package com.example.fastcampusmysql.repository.fixture;

import com.example.fastcampusmysql.domain.dto.PostDto;
import com.example.fastcampusmysql.domain.entity.Post;
import java.time.LocalDate;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.randomizers.text.StringRandomizer;

public class PostFixture {

  static EasyRandom easyRandom;

  static {
    EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
        .excludeField(FieldPredicates.named("id").and(FieldPredicates.ofType(Long.class)))
        .randomize(FieldPredicates.named("contents"), new StringRandomizer())
        .randomize(FieldPredicates.named("memberId"), () -> 1L)
        .dateRange(LocalDate.now().minusDays(3), LocalDate.now());

    easyRandom = new EasyRandom(easyRandomParameters);
  }

  public static Post create()
  {
    Post post = easyRandom.nextObject(Post.class);

    return Post.builder()
        .id(post.getId())
        .memberId(post.getMemberId())
        .contents(post.getContents())
        .createdDate(post.getCreatedDate())
        .build();
  }
}
