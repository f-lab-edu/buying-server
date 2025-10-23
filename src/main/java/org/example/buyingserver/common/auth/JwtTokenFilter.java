package org.example.buyingserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.buyingserver.common.dto.ErrorCodeAndMessage;
import org.example.buyingserver.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtTokenFilter extends GenericFilter {

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();

        try {
            if (path.equals("/member/login") || path.equals("/member/create")) {
                chain.doFilter(request, response);
                return;
            }

            String token = resolveToken(httpRequest);

            if (token != null) {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey.getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();
                UserDetails userDetails = new User(email, "", Collections.emptyList());
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);

        } catch (SignatureException e) {
            sendErrorResponse(httpResponse, ErrorCodeAndMessage.TOKEN_INVALID);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            sendErrorResponse(httpResponse, ErrorCodeAndMessage.TOKEN_EXPIRED);

        } catch (BusinessException e) {
            sendErrorResponse(httpResponse, e.getErrorCodeAndMessage());

        } catch (Exception e) {
            sendErrorResponse(httpResponse, ErrorCodeAndMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Authorization 헤더에서 Bearer 토큰 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null)
            throw new BusinessException(ErrorCodeAndMessage.MISSING_AUTHORIZATION_HEADER);

        if (!bearerToken.startsWith("Bearer "))
            throw new BusinessException(ErrorCodeAndMessage.INVALID_TOKEN_FORMAT);

        return bearerToken.substring(7);
    }

    /**
     * 예외 발생 시 클라이언트에 JSON 형태로 에러 응답 반환
     */
    private void sendErrorResponse(HttpServletResponse response, ErrorCodeAndMessage errorCode) throws IOException {
        response.setStatus(errorCode.getCode());
        response.setContentType("application/json; charset=UTF-8");

        String json = String.format(
                "{\"status\": %d, \"message\": \"%s\"}",
                errorCode.getCode(),
                errorCode.getMessage()
        );
        response.getWriter().write(json);
    }
}
