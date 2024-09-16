package com.example.fastcampusmysql.application;

import com.example.fastcampusmysql.domain.dto.CursorPostDto;
import com.example.fastcampusmysql.domain.dto.Input.CursorPostInput;
import com.example.fastcampusmysql.domain.dto.Input.PostCountInput;
import com.example.fastcampusmysql.domain.dto.Input.PostInput;
import com.example.fastcampusmysql.domain.dto.PostCountDto;
import com.example.fastcampusmysql.domain.dto.PostDto;
import com.example.fastcampusmysql.domain.entity.Post;
import com.example.fastcampusmysql.service.FollowService;
import com.example.fastcampusmysql.service.PostLikeService;
import com.example.fastcampusmysql.service.PostService;
import com.example.fastcampusmysql.service.TimeLineService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostApplication {
  final PostService postService;
  final FollowService followService;
  final TimeLineService timeLineService;
  final PostLikeService postLikeService;

  public PostDto post(PostInput postInput)
  {
    PostDto postDto = postService.post(postInput);
    List<Long> memberIds = followService.getFollowings(postInput.memberId());
    timeLineService.saveAllMemberIdsOfPostId(postDto.id(), memberIds);

    return postDto;
  }

  public List<PostCountDto> getPostCountByDate(PostCountInput postCountInput)
  {
    return postService.getPostCountByDate(postCountInput);
  }

  public PageImpl<Post> getPostsByPage(Long memberId, Pageable pageable)
  {
    return postService.getPostsByPage(memberId, pageable);
  }

  public CursorPostDto getPostsByCursor(Long memberId, CursorPostInput cursorPostInput)
  {
    return postService.getPostsByCursor(
        memberId,
        cursorPostInput.key(),
        cursorPostInput.size()
    );
  }

  public PostDto increaseLikeCountByWriteLock(Long postId)
  {
    return postService.increaseLikeCountByWriteLock(postId);
  }

  public PostDto increaseLikeCountByOptimisticLock(Long postId)
  {
    return postService.increaseLikeCountByOptimisticLock(postId);
  }

  public PostDto postLike(Long memberId, Long postId)
  {
    postLikeService.save(memberId, postId);
    return postService.getPostById(postId);
  }
}
