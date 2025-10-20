package org.example.buyingserver.member.controller;

import lombok.RequiredArgsConstructor;
import org.example.buyingserver.member.domain.Member;
import org.example.buyingserver.member.dto.MemberCreateResponseDto;
import org.example.buyingserver.member.dto.MemberLoginDto;
import org.example.buyingserver.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;



    @PostMapping("/create")
    //TokenResponse 추가할 예정
    public ResponseEntity<?> memberCreate(@RequestBody MemberCreateResponseDto memberCreateResponseDto){
        Member member = memberService.create(memberCreateResponseDto);
        return new ResponseEntity<>(member.getId(), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> memberLogin(@RequestBody MemberLoginDto memberLoginDto){
        Member member = memberService.login(memberLoginDto);

    }

}
