package org.example.buyingserver.common.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Value("${oauth2.frontend.redirect-uri}")
    private String frontendRedirectUri;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        log.debug("OAuth2 SuccessHandler - email={}", email);

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "이메일 정보를 가져올 수 없습니다.");
            return;
        }

        //회원 조회 + 회원 생성 (비즈니스 로직을 서비스로 이관)
        Member member = memberService.findOrCreateOAuthMember(oAuth2User);

        log.debug("OAuth2 SuccessHandler - 로그인 사용자 ID={}", member.getId());

        // JWT 생성
        String accessToken = jwtTokenProvider.createToken(email);

        // 프론트로 리다이렉트
        String encodedToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);
        String redirectUrl = String.format("%s?token=%s&memberId=%d",
                frontendRedirectUri,
                encodedToken,
                member.getId());

        log.debug("OAuth2 SuccessHandler - redirectUrl={}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}