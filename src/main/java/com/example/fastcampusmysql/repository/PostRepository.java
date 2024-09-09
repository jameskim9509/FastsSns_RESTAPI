package com.example.fastcampusmysql.repository;

import com.example.fastcampusmysql.domain.dto.PostCountDto;
import com.example.fastcampusmysql.domain.entity.Post;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import javax.swing.text.html.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public PostRepository(DataSource dataSource)
  {
     namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  public Post save(Long memberId, String contents)
  {
    Post post = Post.builder()
        .memberId(memberId)
        .contents(contents)
        .build();

    String sql = """
        INSERT INTO POST(memberId, contents, createdDate, createdAt) VALUES(:memberId, :contents, :createdDate, :createdAt)
        """;

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("memberId", post.getMemberId())
        .addValue("contents", post.getContents())
        .addValue("createdDate", post.getCreatedDate())
        .addValue("createdAt", post.getCreatedAt());

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder);

    return Post.builder()
        .id(keyHolder.getKey().longValue())
        .memberId(post.getMemberId())
        .contents(post.getContents())
        .createdDate(post.getCreatedDate())
        .createdAt(post.getCreatedAt())
        .build();
  }

  public Optional<List<PostCountDto>> getPosts(Long memberId, LocalDate startDate, LocalDate endDate)
  {
    // 멤버 아이디 비정규화
    String sql = """
        SELECT memberId, createdDate, count(*) AS count
        FROM Post
        WHERE memberId = :memberId AND createdDate BETWEEN :startDate AND :endDate 
        GROUP BY createdDate
        ORDER BY createdDate DESC
        """;
    // Index 필요함

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("memberId", memberId)
        .addValue("startDate", startDate)
        .addValue("endDate", endDate);

    // Dto에 의존
    RowMapper<PostCountDto> rowMapper = (rs, rnum) ->
        new PostCountDto(
          rs.getLong("memberId"),
          rs.getObject("createdDate", LocalDate.class),
          rs.getLong("count")
        );

    return Optional.ofNullable(
        namedParameterJdbcTemplate.query(sql, parameterSource, rowMapper)
    );
  }
}
