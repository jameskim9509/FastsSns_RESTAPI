package com.example.fastcampusmysql.domain.dto;

import com.example.fastcampusmysql.domain.entity.PostLike;

public record PostLikeDto(Long id, Long memberId, Long postId) {
  public static PostLikeDto fromPostLike(PostLike postLike)
  {
    return new PostLikeDto(
        postLike.getId(), postLike.getMemberId(), postLike.getPostId()
    );
  }
}
