package org.example.buyingserver.member.domain;

public enum SocialType {
    GOOGLE,KAKAO, UNKNOWN;

    public static SocialType from(String type) {
        return SocialType.valueOf(type.toUpperCase());
    }
}

