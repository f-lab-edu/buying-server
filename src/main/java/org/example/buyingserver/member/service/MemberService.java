package org.example.buyingserver.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.dto.ErrorCodeAndMessage;
import org.example.buyingserver.common.exception.BusinessException;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.dto.MemberCreateResponseDto;
import org.example.buyingserver.member.dto.MemberLoginDto;
import org.example.buyingserver.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service

public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(MemberCreateResponseDto dto) {
        Member member = Member.builder()
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .nickname(dto.nickname())
                .build();
        return memberRepository.save(member);
    }

    @Transactional
    public Member login(MemberLoginDto memberLoginDto) {
        Member member = memberRepository.findByEmail(memberLoginDto.email())
                .orElseThrow(() -> new BusinessException(ErrorCodeAndMessage.MEMBER_NOT_FOUND));
        if (!passwordEncoder.matches(memberLoginDto.password(), member.getPassword())) {
            throw new BusinessException(ErrorCodeAndMessage.INVALID_PASSWORD);
        }

        return member;
    }

}
