package org.example.buyingserver.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.auth.JwtTokenProvider;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.domain.SocialType;
import org.example.buyingserver.member.dto.AccessTokenDto;
import org.example.buyingserver.member.dto.GoogleProfileDto;
import org.example.buyingserver.member.dto.MemberLoginResponseDto;
import org.example.buyingserver.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleOauthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //구글에서 token받기 인가코드 시크릿 리다이렉트 url ,
    public AccessTokenDto getAccessToken(String code, ) {

    }

    //구글에서 getgoogle 프로필 받기
    public GoogleProfileDto getGoogleProfile(String token) {

    }
    //회원가입해본 애인지 확인하기
    public Member getMemberBySocialId(String socialId) {
      Member member  = memberRepository.findBySocialid(socialId).orElse(null);
      return member;
    }
    //회원가입안한 사람이라면 회원가입
    public Member createOauth(String socialId, String email, SocialType socialType) {
        Member member = Member.oauthCreate(email, socialId, socialType);
        memberRepository.save(member);
        return member;
    }

    @Transactional
    public MemberLoginResponseDto login(String accessToken) {
        GoogleProfileDto googleProfileDto = getGoogleProfile(accessToken);
        Member member = getMemberBySocialId(googleProfileDto.socialId());
        if (member == null) {
            member = createOauth(
                    googleProfileDto.socialId(),
                    googleProfileDto.email(),
                    SocialType.GOOGLE
            );
        }
        String jwtToken = jwtTokenProvider.createToken(member.getEmail());
        return MemberLoginResponseDto.of(member.getId(), jwtToken);
    }
}

