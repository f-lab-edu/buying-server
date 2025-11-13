package org.example.buyingserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.buyingserver.common.dto.ErrorCode;
import org.example.buyingserver.common.dto.ErrorCodeAndMessage;
import org.example.buyingserver.common.exception.BusinessException;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilter {

    @Value("${jwt.secret}")
    private String secretKey;

    private final MemberRepository memberRepository;

    public JwtTokenFilter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();

        try {
            //JWT 검증이 필요 없는 공개 경로들
            if (path.equals("/member/login") ||
                    path.equals("/member/create") ||
                    path.startsWith("/oauth2/") ||
                    path.startsWith("/login/oauth2/") ||
                    path.startsWith("/swagger-ui/") ||
                    path.startsWith("/v3/api-docs") ||
                    path.equals("/favicon.ico") ||
                    path.equals("/error") ||
                    path.startsWith("/posts/lists")) {
                chain.doFilter(request, response);
                return;
            }

            String bearerToken = httpRequest.getHeader("Authorization");
            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                throw new BusinessException(ErrorCodeAndMessage.MISSING_AUTHORIZATION_HEADER);
            }

            String token = bearerToken.substring(7).trim();

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessException(ErrorCodeAndMessage.MEMBER_NOT_FOUND));

            MemberDetails memberDetails = new MemberDetails(member);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    memberDetails,
                    token,
                    memberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);

        } catch (SignatureException e) {
            sendErrorResponse(httpResponse, ErrorCodeAndMessage.TOKEN_INVALID);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            sendErrorResponse(httpResponse, ErrorCodeAndMessage.TOKEN_EXPIRED);

        } catch (BusinessException e) {
            sendErrorResponse(httpResponse, e.getErrorCode());

        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(httpResponse, ErrorCodeAndMessage.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getCode());
        response.setContentType("application/json; charset=UTF-8");

        String json = String.format(
                "{\"status\": %d, \"message\": \"%s\"}",
                errorCode.getCode(),
                errorCode.getMessage());
        response.getWriter().write(json);
    }
}
