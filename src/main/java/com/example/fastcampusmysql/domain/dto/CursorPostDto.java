package com.example.fastcampusmysql.domain.dto;

import com.example.fastcampusmysql.domain.entity.Post;
import java.util.List;

public record CursorPostDto (Integer nextKey, List<Post> posts) {
  public static CursorPostDto fromPostList(Integer key, List<Post> posts)
  {
    return new CursorPostDto(key, posts);
  }
}
