package org.example.buyingserver.member.dto;


public record MemberCreateRequestDto(
     String email,
     String password,
     String nickname
    )
{}