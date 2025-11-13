package org.example.buyingserver.member.service;

import groovy.util.logging.Slf4j;
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


        Optional<Member> existingMember = memberRepository.findByEmail(email);
        if (existingMember.isPresent()) {
            return new DefaultOAuth2User(
                    Collections.emptyList(),
                    oAuth2User.getAttributes(),
                    userNameAttributeName);
        }

        Member newMember = Member.oauthCreate(email, name, socialId, SocialType.GOOGLE);
        Member savedMember = memberRepository.save(newMember);
        entityManager.flush();
        return new DefaultOAuth2User(
                Collections.emptyList(),
                oAuth2User.getAttributes(),
                userNameAttributeName);
    }
}
