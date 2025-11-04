package org.example.buyingserver.common.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
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

        // 2. DB에서 회원 찾기 (이미 CustomOAuth2UserService에서 저장되어 있어야 함)
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("회원 정보를 찾을 수 없습니다."));

        // 3. JWT 생성
        String jwtToken = jwtTokenProvider.createToken(email);

        // 4. 프론트엔드로 JSON 응답 반환
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String jsonResponse = String.format(
                "{\"memberId\": %d, \"accessToken\": \"%s\"}",
                member.getId(),
                jwtToken
        );

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
