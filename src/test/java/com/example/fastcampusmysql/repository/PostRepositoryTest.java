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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class PostRepositoryTest {

  private PostRepository postRepository;
  private List<Post> postList;

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
  void getPostByIndex()
  {
    saveRandomPosts();

    timeCheckTemplate(
        "Get Posts",
        () ->
        {
          String sql = """
              SELECT *
              FROM POST FORCE INDEX(index_created_date)
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

  @Transactional
  @Test
  void paginationByPage()
  {
    saveRandomPosts();
    Pageable pageable = PageRequest.of(12, 3333);

    // default pagination
    timeCheckTemplate(
        "Pagination By Offset",
        () ->
        {
          String sql = String.format(
              """
              SELECT *
              FROM Post
              WHERE memberId=:memberId
              ORDER BY id DESC
              LIMIT :size
              OFFSET :offset
              """
          );

          SqlParameterSource parameterSource = new MapSqlParameterSource()
              .addValue("memberId", 1L)
              .addValue("offset", pageable.getOffset())
              .addValue("size", pageable.getPageSize());

          return postRepository.postQuery(sql, parameterSource);
        }
    );
  }

  @Transactional
  @Test
  void paginationByCoveringIndex()
  {
    saveRandomPosts();
    Pageable pageable = PageRequest.of(12, 3333);

    // Covering Index
    timeCheckTemplate(
        "Covering Index",
        () ->
        {
          // covering index
          String sql = String.format(
              """
              SELECT *
              FROM (SELECT id
                    FROM Post
                    WHERE memberId=:memberId
                    ORDER BY id DESC
                    LIMIT :size
                    OFFSET :offset) as temp
              JOIN Post as p
              ON p.id = temp.id
            """
          );

          SqlParameterSource parameterSource = new MapSqlParameterSource()
              .addValue("memberId", 1L)
              .addValue("offset", pageable.getOffset())
              .addValue("size", pageable.getPageSize());

          return postRepository.postQuery(sql, parameterSource);
        }
    );
  }

  @Transactional
  @Test
  void paginationByCursor()
  {
    int postSize = saveRandomPosts().size();
    Pageable pageable = PageRequest.of(12, 3333);

    //pagination by cursor
    timeCheckTemplate(
        "Pagination By Cursor",
        () ->
        {
          String sql =
              """
              SELECT *
              FROM Post
              WHERE memberId=:memberId AND id<:key
              ORDER BY id DESC
              LIMIT :size
              """;

          SqlParameterSource parameterSource = new MapSqlParameterSource()
              .addValue("memberId", 1L)
              .addValue("key", postSize - pageable.getOffset())
              .addValue("size", pageable.getPageSize());

          return postRepository.postQuery(sql, parameterSource);
        }
    );
  }

  List<Post> saveRandomPosts()
  {
    List<Post> postList =
        (List<Post>) timeCheckTemplate(
            "post 엔티티 리스트 생성",
            () -> IntStream.range(0, 100000)
                .mapToObj(i -> PostFixture.create())
                //  .forEach(System.out::println);
                .collect(Collectors.toList())
        );

    timeCheckTemplate(
        "Post bulk INSERT",
        () ->
        {
          postRepository.saveAll(postList);
          return null;
        }
    );

    // seperate Insert
//    timeCheckTemplate(
//        "post 개별 INSERT",
//        () ->
//        {
//          postList.forEach(p -> postRepository.save(p.getMemberId(), p.getContents()));
//          return null;
//        }
//    );

    return postList;
  }
}