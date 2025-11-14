package org.example.buyingserver.member.service;

import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.common.auth.JwtTokenProvider;
import org.example.buyingserver.member.domain.SocialType;
import org.example.buyingserver.member.dto.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.dto.ErrorCodeAndMessage;
import org.example.buyingserver.common.exception.BusinessException;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new BusinessException(ErrorCodeAndMessage.MEMBER_NOT_FOUND));
        if (!passwordEncoder.matches(memberLoginDto.password(), member.getPassword())) {
            throw new BusinessException(ErrorCodeAndMessage.INVALID_PASSWORD);
        }
        String token = jwtTokenProvider.createToken(member.getEmail());
        return MemberLoginResponseDto.of(member.getId(), token);
    }

    public MemberProfileDto getProfileByToken(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCodeAndMessage.MISSING_AUTHORIZATION_HEADER);
        }
        String token = bearerToken.substring(7);
        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCodeAndMessage.MEMBER_NOT_FOUND));
        return new MemberProfileDto(member.getEmail(), member.getNickname());
    }

    public Member registerOAuthMember(String email, String name, String socialId, SocialType socialType) {
        return memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    //이메일 가진 회원없으면 생성 저장
                    Member newMember = Member.oauthCreate(email, name, socialId, socialType);
                    return memberRepository.save(newMember);
                });
    }



    private void validateDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new BusinessException(ErrorCodeAndMessage.DUPLICATE_EMAIL);
        }
    }
}