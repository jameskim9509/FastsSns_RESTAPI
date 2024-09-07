package com.example.fastcampusmysql.application;

import com.example.fastcampusmysql.domain.dto.FollowDto;
import com.example.fastcampusmysql.domain.dto.FollowingDto;
import com.example.fastcampusmysql.domain.dto.Input.FollowInput;
import com.example.fastcampusmysql.domain.dto.MemberDto;
import com.example.fastcampusmysql.service.FollowService;
import com.example.fastcampusmysql.service.MemberService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowApplication {
  final MemberService memberService;
  final FollowService followService;

  public FollowDto follow(FollowInput input)
  {
    MemberDto fromMember = memberService.findMember(input.fromMemberId());
    MemberDto toMember = memberService.findMember(input.toMemberId());

    return followService.follow(fromMember, toMember);
  }

  public FollowingDto getFollowings(Long memberId)
  {
    MemberDto member = memberService.findMember(memberId);
    List<Long> memberIds = followService.getFollowings(memberId);

    return new FollowingDto(
        memberIds.stream().map(id -> memberService.findMember(id)).collect(Collectors.toList())
    );
  }
}
