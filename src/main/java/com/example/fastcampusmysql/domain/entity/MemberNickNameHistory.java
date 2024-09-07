package com.example.fastcampusmysql.domain.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberNickNameHistory {
  final Long id;
  final Long memberId;
  final String nickName;
  final LocalDateTime createdAt;

  MemberNickNameHistory(Long id, Long memberId, String nickName, LocalDateTime createdAt)
  {
    this.id = id;
    this.memberId = memberId;
    this.nickName = nickName;
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
  }
}
