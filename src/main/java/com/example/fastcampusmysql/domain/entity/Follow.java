package com.example.fastcampusmysql.domain.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Follow {
  final Long id;
  final Long fromMemberId;
  final Long toMemberId;
  final LocalDateTime createdAt;

  Follow(Long id, Long fromMemberId, Long toMemberId, LocalDateTime createdAt)
  {
    this.id = id;
    this.fromMemberId = fromMemberId;
    this.toMemberId = toMemberId;
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
  }
}
