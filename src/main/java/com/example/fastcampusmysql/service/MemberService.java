package com.example.fastcampusmysql.service;

import com.example.fastcampusmysql.domain.dto.MemberDto;
import com.example.fastcampusmysql.domain.dto.Input.RegsiterInput;
import com.example.fastcampusmysql.domain.entity.Member;
import com.example.fastcampusmysql.repository.MemberNickNameHistoryRepository;
import com.example.fastcampusmysql.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
  private final MemberRepository memberRepository;
  private final MemberNickNameHistoryRepository memberNickNameHistoryRepository;

  public MemberDto register(RegsiterInput input)
  {
    Member member = memberRepository.save(
        Member.builder()
            .email(input.email())
            .birth(input.birth())
            .nickName(input.nickName())
            .build()
    );
    memberNickNameHistoryRepository.save(member.getId(), member.getNickName());

    return MemberDto.fromMember(member);
  }

  public MemberDto findMember(Long memberId)
  {
    Member member = memberRepository
        .findMemberById(memberId)
        .orElseThrow(() -> new IllegalArgumentException());

    return MemberDto.fromMember(member);
  }

  public MemberDto changeNickName(Long id, String nickName)
  {
    Member member = memberRepository.changeNickName(id, nickName);
    memberNickNameHistoryRepository.save(id, nickName);

    return MemberDto.fromMember(member);
  }
}
