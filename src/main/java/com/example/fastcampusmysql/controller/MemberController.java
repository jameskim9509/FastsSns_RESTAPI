package com.example.fastcampusmysql.controller;

import com.example.fastcampusmysql.domain.dto.Input.ChangeNickNameInput;
import com.example.fastcampusmysql.domain.dto.Input.RegsiterInput;
import com.example.fastcampusmysql.service.MemberService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
  private final MemberService memberService;

  @PostMapping
  public Object register(@RequestBody RegsiterInput input)
  {
    return memberService.register(input);
  }

  @GetMapping("/{memberId}")
  public Object findMember(@PathVariable("memberId") Long memberId)
  {
    return memberService.findMember(memberId);
  }

  @PutMapping("/{memberId}")
  public Object changeNickName(
      @PathVariable("memberId") Long memberId,
      @RequestBody ChangeNickNameInput changeNickNameInput
  )
  {
    return memberService.changeNickName(memberId, changeNickNameInput.nickName());
  }
}
