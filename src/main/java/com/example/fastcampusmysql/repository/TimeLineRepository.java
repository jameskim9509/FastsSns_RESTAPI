package com.example.fastcampusmysql.repository;

import com.example.fastcampusmysql.domain.entity.TimeLine;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class TimeLineRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public TimeLineRepository(DataSource dataSource)
  {
    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  public void saveAllMemberIdsOfPostId(Long postId, List<Long> memberIds)
  {
    String sql = """
        INSERT INTO TimeLine(postId, memberId, createdAt) VALUES(:postId, :memberId, :createdAt)
        """;

    MapSqlParameterSource[] batchArgs = memberIds.stream()
        .map(memberId -> TimeLine.builder()
            .postId(postId)
            .memberId(memberId)
            .build())
        .map(tl -> new MapSqlParameterSource()
            .addValue("memberId", tl.getMemberId())
            .addValue("postId", tl.getPostId())
            .addValue("createdAt", tl.getCreatedAt()))
        .toArray(MapSqlParameterSource[]::new);

    namedParameterJdbcTemplate.batchUpdate(sql, batchArgs);
  }

  public List<TimeLine> getTimeLinesInMemberIds(Long memberId)
  {
    String sql = """
        SELECT *
        FROM TimeLine
        WHERE memberId=:memberId
        ORDER BY id DESC
        """;

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("memberId", memberId);

    RowMapper<TimeLine> rowMapper = (rs, rowNum) ->
      TimeLine.builder()
          .id(rs.getLong("id"))
          .memberId(rs.getLong("memberId"))
          .postId(rs.getLong("postId"))
          .createdAt(rs.getObject("createdAt", LocalDateTime.class))
          .build();

    return namedParameterJdbcTemplate.query(sql, parameterSource, rowMapper);
  }
}
