package org.example.buyingserver.member.dto;

import org.example.buyingserver.member.domain.Member;

public record MemberCardDto(
        Long id,
        String nickname
) {
    public static MemberCardDto from(Member member) {
        return new MemberCardDto(member.getId(), member.getNickname());
    }
}