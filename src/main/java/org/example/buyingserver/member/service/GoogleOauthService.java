package org.example.buyingserver.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.auth.JwtTokenProvider;
import org.example.buyingserver.common.dto.ErrorCodeAndMessage;
import org.example.buyingserver.common.exception.BusinessException;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.domain.SocialType;
import org.example.buyingserver.member.dto.AccessTokenDto;
import org.example.buyingserver.member.dto.GoogleProfileDto;
import org.example.buyingserver.member.dto.MemberLoginResponseDto;
import org.example.buyingserver.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class GoogleOauthService {
//    private final MemberRepository memberRepository;
//    private final JwtTokenProvider jwtTokenProvider;

//    //구글에서 token받기 인가코드 시크릿 리다이렉트 url
//    @Value("${google.client-id}")
//    private String clientId;
//
//    @Value("${google.client-secret}")
//    private String clientSecret;
//
//    @Value("${google.redirect-uri}")
//    private String redirectUri;
//
//    @Value("${google.token-uri}")
//    private String tokenUri;
//
//    @Value("${google.user-info-uri}")
//    private String userInfoUri;
//
//
//    public AccessTokenDto getAccessToken(String code) {
//        RestClient restClient = RestClient.create();
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("code", code);
//        params.add("client_id", clientId);
//        params.add("client_secret", clientSecret);
//        params.add("redirect_uri", redirectUri);
//        params.add("grant_type", "authorization_code");
//
//        ResponseEntity<Map> response = restClient.post()
//                .uri(tokenUri)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .body(params)
//                .retrieve()
//                .toEntity(Map.class);
//
//        Map<String, Object> body = response.getBody();
//        if (body == null || body.get("access_token") == null) {
//            throw new BusinessException(ErrorCodeAndMessage.GOOGLE_TOKEN_REQUEST_FAILED);
//        }
//        String accessToken = (String) body.get("access_token");
//        return new AccessTokenDto(accessToken);
//    }
//
//    public GoogleProfileDto getGoogleProfile(String token) {
//        RestClient restClient = RestClient.create();
//        ResponseEntity<GoogleProfileDto> response = restClient.get()
//                .uri(userInfoUri)
//                .header("Authorization", "Bearer " + token)
//                .retrieve()
//                .toEntity(GoogleProfileDto.class);
//
//        return response.getBody();
//    }
//
//    //회원가입해본 애인지 확인하기
//    public Member getMemberBySocialId(String socialId) {
//      Member member  = memberRepository.findBySocialid(socialId).orElse(null);
//      return member;
//    }
//    //회원가입안한 사람이라면 회원가입
//    public Member createOauth(String socialId, String name, String email, SocialType socialType) {
//        Member member = Member.oauthCreate(email, name, socialId, socialType);
//        memberRepository.save(member);
//        return member;
//    }
//
//    @Transactional
//    public MemberLoginResponseDto login(String accessToken) {
//        GoogleProfileDto googleProfileDto = getGoogleProfile(accessToken);
//        Member member = getMemberBySocialId(googleProfileDto.sub());
//        if (member == null) {
//            member = createOauth(
//                    googleProfileDto.sub(),
//                    googleProfileDto.name(),
//                    googleProfileDto.email(),
//                    SocialType.GOOGLE
//            );
//        }
//        String jwtToken = jwtTokenProvider.createToken(member.getEmail());
//        return MemberLoginResponseDto.of(member.getId(), jwtToken);
//    }
//}

