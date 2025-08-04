package com.be16_2nd.SmartFridge.member.controller;

import com.be16_2nd.SmartFridge.common.auth.JwtTokenProvider;
import com.be16_2nd.SmartFridge.common.dto.CommonDto;
import com.be16_2nd.SmartFridge.common.dto.CommonErrorDto;
import com.be16_2nd.SmartFridge.member.domain.Member;
import com.be16_2nd.SmartFridge.member.dto.LoginReqDto;
import com.be16_2nd.SmartFridge.member.dto.LoginResDto;
import com.be16_2nd.SmartFridge.member.dto.MemberCreateDto;
import com.be16_2nd.SmartFridge.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody @Valid MemberCreateDto memberCreateDto){
        Long id = memberService.save(memberCreateDto);
        return new ResponseEntity<>(
                CommonDto.builder()
                        .result(id)
                        .status_code(HttpStatus.OK.value())
                        .status_message("회원가입을 축하합니다!")
                        .build(),HttpStatus.CREATED);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody LoginReqDto loginReqDto){
        Member member = memberService.doLogin(loginReqDto);
        String accessToken = jwtTokenProvider.createAtToken(member);

        LoginResDto loginResDto = LoginResDto.builder()
                .accessToken(accessToken)
                .refreshToken(null)
                .build();

        return new ResponseEntity<>(
                CommonDto.builder()
                        .result(loginResDto)
                        .status_code(HttpStatus.OK.value())
                        .status_message("로그인 성공")
                        .build(), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(){
        memberService.delete();
        return new ResponseEntity<>(
                CommonDto.builder()
                        .result("OK")
                        .status_code(HttpStatus.OK.value())
                        .status_message("회원탈퇴가 성공적으로 되었습니다. \n" +
                                "그동안 저희 Smart Fridge를 이용해주셔서 감사합니다.")
                        .build(), HttpStatus.OK);
    }
}
