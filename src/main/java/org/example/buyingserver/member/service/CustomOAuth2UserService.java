package org.example.buyingserver.member.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.domain.SocialType;
import org.example.buyingserver.member.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        System.out.println("[DEBUG] CustomOAuth2UserService.loadUser 실행 시작");
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String socialId = oAuth2User.getAttribute(userNameAttributeName);

        System.out.println(
                "[DEBUG] CustomOAuth2UserService - email: " + email + ", name: " + name + ", socialId: " + socialId);

        Optional<Member> existingMember = memberRepository.findByEmail(email);
        if (existingMember.isPresent()) {
            System.out.println("[DEBUG] CustomOAuth2UserService - 기존 회원 발견: " + email);
            return new DefaultOAuth2User(
                    Collections.emptyList(),
                    oAuth2User.getAttributes(),
                    userNameAttributeName);
        }

        System.out.println("[DEBUG] CustomOAuth2UserService - 새 회원 생성 시작: " + email);
        Member newMember = Member.oauthCreate(email, name, socialId, SocialType.GOOGLE);
        Member savedMember = memberRepository.save(newMember);
        // 트랜잭션이 커밋되기 전에 DB에 반영되도록 flush
        entityManager.flush();
        System.out.println(
                "[DEBUG] CustomOAuth2UserService - 회원 생성 완료 및 flush: " + email + ", ID: " + savedMember.getId());

        return new DefaultOAuth2User(
                Collections.emptyList(),
                oAuth2User.getAttributes(),
                userNameAttributeName);
    }
}
