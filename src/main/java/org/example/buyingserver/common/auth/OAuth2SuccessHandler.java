package org.example.buyingserver.common.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.domain.SocialType;
import org.example.buyingserver.member.service.MemberService;
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
    private final MemberService memberService;

    @Value("${oauth2.frontend.redirect-uri}")
    private String frontendRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String socialId = oAuth2User.getAttribute("sub");

        Member member = memberService.registerOAuthMember(
                email,
                name != null ? name : email.split("@")[0],
                socialId,
                SocialType.GOOGLE
        );

        String accessToken = jwtTokenProvider.createToken(member.getEmail());

        String encodedToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);
        String redirectUrl = String.format(
                "%s?token=%s&memberId=%d",
                frontendRedirectUri,
                encodedToken,
                member.getId()
        );

        response.sendRedirect(redirectUrl);
    }
}
