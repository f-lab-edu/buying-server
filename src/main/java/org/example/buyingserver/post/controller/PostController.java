package org.example.buyingserver.post.controller;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.common.dto.ApiResponse;
import org.example.buyingserver.common.dto.ResponseCodeAndMessage;
import org.example.buyingserver.common.dto.ResponseCodePostAndMessage;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.post.dto.PostCreateRequestDto;
import org.example.buyingserver.post.dto.PostCreateResponseDto;
import org.example.buyingserver.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostCreateResponseDto>> postCreate(
            @RequestBody PostCreateRequestDto requestDto,
            @AuthenticationPrincipal Member member
    ) {
        PostCreateResponseDto response = postService.create(requestDto, member);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(ResponseCodePostAndMessage.SUCCESS_POST_CREATED, response));
    }
}
