package com.example.fastcampusmysql.domain.dto;

import com.example.fastcampusmysql.domain.entity.Member;
import java.time.LocalDate;

public record MemberDto(Long id, String email, String nickName, LocalDate birth) {

  public static MemberDto fromMember(Member member)
  {
    return new MemberDto(member.getId(), member.getEmail(), member.getNickName(), member.getBirth());
  }
}
