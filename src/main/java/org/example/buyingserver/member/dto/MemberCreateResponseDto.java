package org.example.buyingserver.member.dto;


public record MemberCreateResponseDto (
     String email,
     String password,
     String nickname
    )
{}