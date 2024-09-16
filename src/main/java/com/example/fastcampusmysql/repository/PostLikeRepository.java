package com.example.fastcampusmysql.repository;

import com.example.fastcampusmysql.domain.entity.PostLike;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class PostLikeRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public PostLikeRepository(DataSource dataSource)
  {
    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  public PostLike save(Long memberId, Long postId)
  {
    PostLike postLike = PostLike.builder()
        .memberId(memberId)
        .postId(postId)
        .build();

    String sql = """
        INSERT INTO 
        PostLike(memberId, postId, createdAt)
        VALUES(:memberId, :postId, :createdAt)
        """;

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("memberId", memberId)
        .addValue("postId", postId)
        .addValue("createdAt", postLike.getCreatedAt());

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder);

    return PostLike.builder()
        .id(keyHolder.getKey().longValue())
        .memberId(postLike.getMemberId())
        .postId(postLike.getPostId())
        .createdAt(postLike.getCreatedAt())
        .build();
  }

  public Long count(Long postId)
  {
    String sql = """
        SELECT count(*) AS count
        FROM PostLike
        WHERE postId=:postId
        """;

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("postId", postId);

    RowMapper<Long> rowMapper = (rs, rnum) ->
        rs.getLong("count");

    return namedParameterJdbcTemplate
        .queryForObject(sql, parameterSource, rowMapper);
  }
}
