package com.example.fastcampusmysql.controller;

import com.example.fastcampusmysql.domain.dto.Input.CursorPostInput;
import com.example.fastcampusmysql.domain.dto.Input.PostCountInput;
import com.example.fastcampusmysql.domain.dto.Input.PostInput;
import com.example.fastcampusmysql.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
  private final PostService postService;

  @PostMapping
  public Object post(@RequestBody PostInput postInput)
  {
    return postService.post(postInput);
  }

  @GetMapping("/count")
  public Object getPosts(@ModelAttribute  PostCountInput postCountInput)
  {
    return postService.getPostCountByDate(postCountInput);
  }

  @GetMapping("/page/{memberId}")
  public Object getPostsByPage(
      @PathVariable("memberId") Long memberId,
      Pageable pageable // vs @ModelAttribute Pageable pageable
  )
  {
    return postService.getPostsByPage(memberId, pageable);
  }

  @GetMapping("/cursor/{memberId}")
  public Object getPostsByCursor(
      @PathVariable("memberId") Long memberId,
      @ModelAttribute CursorPostInput cursorPostInput
  )
  {
    return postService.getPostsByCursor(memberId, cursorPostInput.key(), cursorPostInput.size());
  }
}
