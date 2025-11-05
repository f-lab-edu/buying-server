package org.example.buyingserver.member.controller;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.auth.JwtTokenProvider;
import org.example.buyingserver.common.auth.MemberDetails;
import org.example.buyingserver.common.dto.ApiResponse;
import org.example.buyingserver.common.dto.ResponseCodeAndMessage;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.dto.*;
//import org.example.buyingserver.member.service.GoogleOauthService;
import org.example.buyingserver.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
   // private final GoogleOauthService googleOauthService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원 가입
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MemberCreateResponseDto>> memberCreate(
            @RequestBody MemberCreateRequestDto requestDto
    ) {
        MemberCreateResponseDto dto = memberService.create(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(ResponseCodeAndMessage.MEMBER_CREATED, dto));
    }

    /**
     * 일반 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberLoginResponseDto>> memberLogin(
            @RequestBody MemberLoginDto memberLoginDto
    ) {
        MemberLoginResponseDto dto = memberService.login(memberLoginDto);

        return ResponseEntity.ok(
                ApiResponse.success(ResponseCodeAndMessage.AUTH_SUCCESS, dto)
        );
    }

    /**
     * 구글 로그인 (OAuth)
     */
//    @PostMapping("/google/login")
//    public ResponseEntity<ApiResponse<MemberLoginResponseDto>> googleLogin(
//            @RequestBody RedirectDto redirectDto
//    ) {
//        AccessTokenDto accessTokenDto = googleOauthService.getAccessToken(redirectDto.code());
//        MemberLoginResponseDto dto = googleOauthService.login(accessTokenDto.accessToken());
//
//        return ResponseEntity.ok(
//                ApiResponse.success(ResponseCodeAndMessage.AUTH_SUCCESS, dto)
//        );
//    }

    /**
     * 프로필 조회
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<MemberProfileDto>> getProfile(
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        Member member = memberDetails.getMember();

        MemberProfileDto dto = new MemberProfileDto(
                member.getEmail(),
                member.getNickname()
        );

        return ResponseEntity.ok(
                ApiResponse.success(ResponseCodeAndMessage.MEMBER_FOUND, dto)
        );
    }
}
