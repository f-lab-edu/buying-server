package org.example.buyingserver.common.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Value("${oauth2.frontend.redirect-uri}")
    private String frontendRedirectUri;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        // 1. OAuth2 로그인 완료된 사용자 정보 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "이메일 정보를 가져올 수 없습니다.");
            return;
        }

        // 2. DB에서 회원 찾기
        Member member = memberRepository.findByEmail(email).orElse(null);

        // 회원이 없으면 생성 (CustomOAuth2UserService에서 생성되지 않은 경우 대비)
        if (member == null) {
            String name = oAuth2User.getAttribute("name");
            String socialId = oAuth2User.getAttribute("sub"); // Google의 userNameAttributeName

            member = Member.oauthCreate(
                    email,
                    name != null ? name : email.split("@")[0],
                    socialId,
                    org.example.buyingserver.member.domain.SocialType.GOOGLE);
            member = memberRepository.save(member);
        } else {
        }

        // 3. JWT 생성
        String accessToken = jwtTokenProvider.createToken(email);

        // 4. 프론트엔드로 리다이렉트하면서 토큰 전달
        String encodedToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);
        String redirectUrl = String.format("%s?token=%s&memberId=%d",
                frontendRedirectUri,
                encodedToken,
                member.getId());

        response.sendRedirect(redirectUrl);
    }
}
