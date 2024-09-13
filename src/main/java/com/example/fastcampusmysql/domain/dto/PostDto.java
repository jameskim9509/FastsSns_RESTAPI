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

  public static Post toPost(PostDto postDto)
  {
    return Post.builder()
//        .id(postDto.id())
        .memberId(postDto.memberId())
        .contents(postDto.contents())
        .createdDate(postDto.createdDate)
        .build();
  }
}
