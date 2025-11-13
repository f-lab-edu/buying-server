package org.example.buyingserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.dto.ErrorCodeAndMessage;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);

        Claims claims = parseClaims(token);

        setAuthentication(claims, token);

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return path.equals("/member/login") ||
                path.equals("/member/create") ||
                path.startsWith("/oauth2/") ||
                path.startsWith("/login/oauth2/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs") ||
                path.equals("/favicon.ico") ||
                path.equals("/error") ||
                path.startsWith("/posts/lists");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new JwtAuthenticationException(ErrorCodeAndMessage.MISSING_AUTHORIZATION_HEADER);
        }

        return bearerToken.substring(7).trim();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new JwtAuthenticationException(ErrorCodeAndMessage.TOKEN_EXPIRED);
        } catch (SignatureException e) {
            throw new JwtAuthenticationException(ErrorCodeAndMessage.TOKEN_INVALID);
        } catch (Exception e) {
            throw new JwtAuthenticationException(ErrorCodeAndMessage.TOKEN_INVALID);
        }
    }

    private void setAuthentication(Claims claims, String token) {
        String email = claims.getSubject();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new JwtAuthenticationException(ErrorCodeAndMessage.MEMBER_NOT_FOUND));

        MemberDetails memberDetails = new MemberDetails(member);

        Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                memberDetails,
                token,
                memberDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}