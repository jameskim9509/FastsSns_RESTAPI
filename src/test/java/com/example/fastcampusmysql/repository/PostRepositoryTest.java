package com.example.fastcampusmysql.repository;

import com.example.fastcampusmysql.domain.entity.Post;
import com.example.fastcampusmysql.repository.fixture.PostFixture;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class PostRepositoryTest {

  private PostRepository postRepository;

  @Autowired
  PostRepositoryTest(PostRepository postRepository)
  {
    this.postRepository = postRepository;
  }

  Object timeCheckTemplate(String title, Supplier callback)
  {
    long beforeTime = System.currentTimeMillis();
    Object result = callback.get();
    System.out.println(title + "  => 걸린 시간 : " + (System.currentTimeMillis() - beforeTime)/1000.0 + "초");

    return result;
  }

  @Transactional
  @Test
  void saveAll() {
    List<Post> postList =
        (List<Post>) timeCheckTemplate(
            "post 엔티티 리스트 생성",
            () -> IntStream.range(0, 100000)
                .mapToObj(i -> PostFixture.create())
            //  .forEach(System.out::println);
                .collect(Collectors.toList())
    );

//    timeCheckTemplate(
//        "post 개별 INSERT",
//        () ->
//        {
//          postList.forEach(p -> postRepository.save(p.getMemberId(), p.getContents()));
//          return null;
//        }
//    );


    timeCheckTemplate(
        "Post bulk INSERT",
        () ->
        {
          postRepository.saveAll(postList);
          return null;
        }
    );

    timeCheckTemplate(
        "Get Posts",
        () ->
        {
          String sql = """
              SELECT *
              FROM POST FORCE INDEX(index_member_id)
              WHERE memberId=:memberId and createdDate between :startDate and :endDate
              """;

//          String sql = """
//              SELECT *
//              FROM POST FORCE INDEX(PRIMARY)
//              WHERE memberId=:memberId and createdDate between :startDate and :endDate
//              """;

          SqlParameterSource parameterSource = new MapSqlParameterSource()
              .addValue("memberId", 1L)
              .addValue("startDate", LocalDateTime.now().minusDays(1))
              .addValue("endDate", LocalDateTime.now());

          return postRepository.postQuery(sql, parameterSource);
        });
  }
}