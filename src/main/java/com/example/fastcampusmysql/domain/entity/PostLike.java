package com.example.fastcampusmysql.domain.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostLike {
  final Long id;
  final Long postId;
  final Long memberId;
  final LocalDateTime createdAt;

  PostLike(Long id, Long postId, Long memberId, LocalDateTime createdAt)
  {
    this.id = id;
    this.postId = postId;
    this.memberId = memberId;
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
  }
}
