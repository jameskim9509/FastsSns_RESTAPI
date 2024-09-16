package com.example.fastcampusmysql.service;

import com.example.fastcampusmysql.domain.dto.PostLikeDto;
import com.example.fastcampusmysql.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService {
  private final PostLikeRepository postLikeRepository;

  public PostLikeDto save(Long memberId, Long postId)
  {
    return PostLikeDto.fromPostLike(
        postLikeRepository.save(memberId, postId)
    );
  }

  public Long count(Long postId)
  {
    return postLikeRepository.count(postId);
  }
}
