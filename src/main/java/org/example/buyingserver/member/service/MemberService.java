package org.example.buyingserver.member.service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.auth.JwtTokenProvider;
import org.example.buyingserver.member.domain.SocialType;
import org.example.buyingserver.member.dto.*;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.exception.*;
import org.example.buyingserver.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public MemberCreateResponseDto create(MemberCreateRequestDto dto) {

        validateDuplicateEmail(dto.email());

        Member member = Member.create(
                dto.email(),
                dto.password(),
                dto.nickname(),
                passwordEncoder
        );

        Member savedMember = memberRepository.save(member);

        return MemberCreateResponseDto.of(
                savedMember.getId(),
                savedMember.getEmail(),
                savedMember.getNickname()
        );
    }


    @Transactional(readOnly = true)
    public MemberLoginResponseDto login(MemberLoginDto memberLoginDto) {

        Member member = memberRepository.findByEmail(memberLoginDto.email())
                .orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(memberLoginDto.password(), member.getPassword())) {
            throw new InvalidPasswordException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return MemberLoginResponseDto.of(member.getId(), token);
    }


    public MemberProfileDto getProfileByToken(String bearerToken) {

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new MissingAuthHeaderException();
        }

        String token = bearerToken.substring(7);
        String email = jwtTokenProvider.getEmailFromToken(token);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        return new MemberProfileDto(member.getEmail(), member.getNickname());
    }

    @Transactional
    public Member findOrCreateOAuthMember(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String socialId = extractSocialId(oAuth2User);
        String nickname = (name != null) ? name : email.split("@")[0];
        SocialType socialType = detectSocialType(oAuth2User);


        return memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    Member newMember = Member.oauthCreate(
                            email,
                            nickname,
                            socialId,
                            socialType
                    );
                    return memberRepository.save(newMember);
                });
    }


    private void validateDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException();
        }
    }

    private String extractSocialId(OAuth2User oAuth2User) {
        String socialId = oAuth2User.getAttribute("sub"); // Google
        if (socialId == null) {
            socialId = oAuth2User.getAttribute("id");
        }
        return socialId;
    }

    private SocialType detectSocialType(OAuth2User oAuth2User) {
        if (oAuth2User.getAttribute("sub") != null) {
            return SocialType.GOOGLE;
        }
        if (oAuth2User.getAttribute("id") != null) {
            return SocialType.KAKAO;
        }
        return SocialType.UNKNOWN;

    }
}