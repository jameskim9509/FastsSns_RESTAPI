package com.example.fastcampusmysql.domain.dto;

import com.example.fastcampusmysql.domain.entity.Post;
import java.time.LocalDate;

public record PostDto(Long id, Long memberId, String contents, LocalDate createdDate) {
  public static PostDto fromPost(Post post)
  {
    return new PostDto(
        post.getId(),
        post.getMemberId(),
        post.getContents(),
        post.getCreatedDate()
    );
  }
}
