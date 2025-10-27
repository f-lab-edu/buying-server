package org.example.buyingserver.member.dto;

public record MemberCreateResponseDto(
        Long id,
        String email,
        String nickname
) {
    public static MemberCreateResponseDto of(Long id, String email, String nickname) {
        return new MemberCreateResponseDto(id, email, nickname);
    }
}