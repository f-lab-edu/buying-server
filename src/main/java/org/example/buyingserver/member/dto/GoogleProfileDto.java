package org.example.buyingserver.member.dto;

public record GoogleProfileDto(
        String email,
        String name,
        String picture,
        String socialId
) {
}
