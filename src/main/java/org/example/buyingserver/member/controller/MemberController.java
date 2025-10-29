package org.example.buyingserver.member.controller;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.auth.JwtTokenProvider;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.domain.SocialType;
import org.example.buyingserver.member.dto.*;
import org.example.buyingserver.member.service.GoogleOauthService;
import org.example.buyingserver.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final GoogleOauthService googleOauthService;
    private final JwtTokenProvider jwtTokenProvider;
    // private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/create")
    //TokenResponse 추가할 예정
    public ResponseEntity<?> memberCreate(@RequestBody MemberCreateRequestDto memberCreateResponseDto){
        MemberCreateResponseDto dto = memberService.create(memberCreateResponseDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> memberLogin(@RequestBody MemberLoginDto memberLoginDto){
        MemberLoginResponseDto response = memberService.login(memberLoginDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/google/login")
    //로그인할때 받아올 코드값
    public ResponseEntity<?> googleLogin(@RequestBody RedirectDto redirectDto) {
        AccessTokenDto accessTokenDto = googleOauthService.getAccessToken(redirectDto.code());
        MemberLoginResponseDto response = googleOauthService.login(accessTokenDto.accessToken());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberProfileDto>> getCurrentUser(Authentication authentication) {
        // 현재 인증된 사용자 이메일 가져오기
        String email = authentication.getName();

        // 서비스에서 사용자 정보 조회
        MemberProfileDto profile = memberService.getProfileByEmail(email);

        // 공통 응답 형태로 반환
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

}
