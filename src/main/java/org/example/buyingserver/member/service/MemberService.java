package org.example.buyingserver.member.service;

import lombok.extern.slf4j.Slf4j;
import org.example.buyingserver.common.auth.JwtTokenProvider;
import org.example.buyingserver.member.dto.MemberCreateResponseDto;
import org.example.buyingserver.member.dto.MemberLoginResponseDto;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.dto.ErrorCodeAndMessage;
import org.example.buyingserver.common.exception.BusinessException;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.dto.MemberCreateRequestDto;
import org.example.buyingserver.member.dto.MemberLoginDto;
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
        Member member = Member.builder()
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .nickname(dto.nickname())
                .build();
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

    private void validateDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new BusinessException(ErrorCodeAndMessage.DUPLICATE_EMAIL);
        }
    }
}