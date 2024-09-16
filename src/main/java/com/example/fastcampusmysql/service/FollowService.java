package com.example.fastcampusmysql.service;

import com.example.fastcampusmysql.domain.dto.FollowDto;
import com.example.fastcampusmysql.domain.dto.FollowingDto;
import com.example.fastcampusmysql.domain.dto.MemberDto;
import com.example.fastcampusmysql.domain.entity.Follow;
import com.example.fastcampusmysql.repository.FollowRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
  final FollowRepository followRepository;

  public FollowDto follow(MemberDto fromMember, MemberDto toMember)
  {
    followRepository.save(fromMember, toMember);
    return new FollowDto(fromMember, toMember);
  }

  public List<Long> getFollowings(Long memberId)
  {
    List<Follow> follows = followRepository.findAllByToMemberId(memberId);
    if(follows.isEmpty()) throw new IllegalArgumentException();

    return follows.stream()
        .map(f -> f.getFromMemberId())
        .collect(Collectors.toList());
  }

  public List<Long> getFollowers(Long memberId)
  {
    List<Follow> follows = followRepository.findAllByFromMemberId(memberId);
    if(follows.isEmpty()) throw new IllegalArgumentException();

    return follows.stream()
        .map(f -> f.getToMemberId())
        .collect(Collectors.toList());
  }
}
