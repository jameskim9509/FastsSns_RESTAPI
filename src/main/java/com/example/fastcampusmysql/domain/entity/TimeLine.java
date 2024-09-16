package com.example.fastcampusmysql.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

//@AllArgsConstructor
@Getter
@Builder
public class TimeLine {
  final Long id;
  final Long postId;
  final Long memberId;
  final LocalDateTime createdAt;

  TimeLine(Long id, Long postId, Long memberId, LocalDateTime createdAt)
  {
    this.id = id;
    this.postId = postId;
    this.memberId = memberId;
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
  }
}
