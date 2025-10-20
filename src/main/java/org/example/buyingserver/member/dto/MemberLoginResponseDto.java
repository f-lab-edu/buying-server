package org.example.buyingserver.member.dto;

public record MemberLoginResponseDto(
        Long memberId,
        String accessToken
) {
    public static MemberLoginResponseDto of(Long memberId, String token) {
        return new MemberLoginResponseDto(memberId, token);
    }
}