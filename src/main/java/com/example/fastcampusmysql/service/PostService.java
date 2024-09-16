package com.example.fastcampusmysql.service;

import com.example.fastcampusmysql.domain.dto.CursorPostDto;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  public List<PostDto> getPostsByIdsOrderByIdDESC(List<Long> ids)
  {
    return postRepository.getPostsByIdsOrderByIdDESC(ids).stream()
        .map(PostDto::fromPost)
        .collect(Collectors.toList());
  }

  public List<PostDto> getPostsByMemberList(List<Long> memberIds)
  {
    return postRepository.getPostsByMemberIds(memberIds).stream()
        .map(PostDto::fromPost)
        .collect(Collectors.toList());
  }

  public List<PostCountDto> getPostCountByDate(PostCountInput input)
  {
    return postRepository
        .getPosts(input.memberId(), input.startDate(), input.endDate())
        .orElseThrow(IllegalArgumentException::new);
  }

  public PageImpl<Post> getPostsByPage(Long memberId, Pageable pageable)
  {
    return postRepository.getPostsByPage(memberId, pageable);
  }

  public CursorPostDto getPostsByCursor(Long memberId, Integer key, Integer size)
  {
    List<Post> posts = postRepository.getPostsByCursor(memberId, key, size);
    Integer nextKey = key == null ? posts.stream()
        .map(p -> Math.toIntExact(p.getId()))
        .min((i1, i2) -> i1- i2)
        .get()
        : key-posts.size();

    return new CursorPostDto(nextKey, posts);
  }
}
