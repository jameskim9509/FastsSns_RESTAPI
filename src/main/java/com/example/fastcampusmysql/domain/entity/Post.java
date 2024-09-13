package com.example.fastcampusmysql.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString // forTest
public class Post {
  final Long id;
  final Long memberId;
  String contents;
  final LocalDate createdDate;
  final LocalDateTime createdAt;

  public Post(Long id, Long memberId, String contents, LocalDate createdDate, LocalDateTime createdAt)
  {
    this.id = id;
    this.memberId = memberId;
    this.contents = contents;
    this.createdDate = createdDate == null ? LocalDate.now() : createdDate;
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
  }
}
