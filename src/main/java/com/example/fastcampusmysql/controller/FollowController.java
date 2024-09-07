package com.example.fastcampusmysql.controller;

import com.example.fastcampusmysql.application.FollowApplication;
import com.example.fastcampusmysql.domain.dto.Input.FollowInput;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
  final FollowApplication followApplication;

  @PostMapping
  public Object follow(@RequestBody FollowInput input)
  {
    return followApplication.follow(input);
  }

  @GetMapping("/{memberId}")
  public Object getFollowings(@PathVariable("memberId") Long memberId)
  {
    return followApplication.getFollowings(memberId);
  }
}
