package com.example.fastcampusmysql.service;

import com.example.fastcampusmysql.domain.dto.CursorPostDto;
import com.example.fastcampusmysql.domain.dto.Input.PostCountInput;
import com.example.fastcampusmysql.domain.dto.Input.PostInput;
import com.example.fastcampusmysql.domain.dto.PostCountDto;
import com.example.fastcampusmysql.domain.dto.PostDto;
import com.example.fastcampusmysql.domain.entity.Post;
import com.example.fastcampusmysql.repository.PostLikeRepository;
import com.example.fastcampusmysql.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {
  final PostRepository postRepository;
  final PostLikeRepository postLikeRepository;

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

  public PostDto getPostById(Long id)
  {
    return PostDto.fromPost(
        postRepository.getPostById(id, false)
            .orElseThrow(IllegalArgumentException::new)
            // post를 조회할 때마다 count 쿼리 발생
            .setLikeCount(postLikeRepository.count(id))
    );
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

  @Transactional // AutoCommit을 false로 만들고 하나의 트랜잭션으로 취급 o
  public PostDto increaseLikeCountByWriteLock(Long postId)
  {
    Post post = postRepository.getPostById(postId, true).orElseThrow(IllegalArgumentException::new);
    post.setLikeCount(post.getLikeCount() + 1);
    return PostDto.fromPost(
        postRepository.update(post).orElseThrow(RuntimeException::new)
    );
  }

  @Transactional // for Rollback (예외 발생시)
  public PostDto increaseLikeCountByOptimisticLock(Long postId)
  {
    Post post = postRepository.getPostById(postId, false).orElseThrow(IllegalArgumentException::new);
    post.setLikeCount(post.getLikeCount() + 1);;
    return PostDto.fromPost(
        postRepository.update(post).orElseThrow(RuntimeException::new)
    );
  }
}
