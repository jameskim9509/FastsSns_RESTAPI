package com.example.fastcampusmysql.service;

import com.example.fastcampusmysql.domain.dto.Input.PostCountInput;
import com.example.fastcampusmysql.domain.dto.Input.PostInput;
import com.example.fastcampusmysql.domain.dto.PostCountDto;
import com.example.fastcampusmysql.domain.dto.PostDto;
import com.example.fastcampusmysql.domain.entity.Post;
import com.example.fastcampusmysql.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {
  final PostRepository postRepository;

  public PostDto post(PostInput input)
  {
    Post post = postRepository.save(input.memberId(), input.contents());

    return PostDto.fromPost(post);
  }

  // for Test
  public void bulkInsert(List<PostDto> postDtos)
  {
    List<Post> posts = postDtos.stream()
        .map(PostDto::toPost)
        .collect(Collectors.toList());

    postRepository.saveAll(posts);
  }

  public List<PostCountDto> getPostCountByDate(PostCountInput input)
  {
    return postRepository
        .getPosts(input.memberId(), input.startDate(), input.endDate())
        .orElseThrow(IllegalArgumentException::new);
  }
}
