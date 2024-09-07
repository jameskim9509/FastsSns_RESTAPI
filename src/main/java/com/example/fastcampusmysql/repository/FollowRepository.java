package com.example.fastcampusmysql.repository;

import com.example.fastcampusmysql.domain.dto.MemberDto;
import com.example.fastcampusmysql.domain.entity.Follow;
import com.example.fastcampusmysql.domain.entity.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class FollowRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  FollowRepository(DataSource dataSource)
  {
    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  public Follow save(MemberDto fromMember, MemberDto toMember)
  {
    Follow follow = Follow.builder()
        .fromMemberId(fromMember.id())
        .toMemberId(toMember.id())
        .build();

    String sql = """
        INSERT INTO FOLLOW(fromMemberId, toMemberId, createdAt) VALUES(:fromMemberId, :toMemberId, :createdAt)
        """;

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("fromMemberId", fromMember.id())
        .addValue("toMemberId", toMember.id())
        .addValue("createdAt", follow.getCreatedAt());

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder);

    return Follow.builder()
        .id(keyHolder.getKey().longValue())
        .fromMemberId(fromMember.id())
        .toMemberId(toMember.id())
        .build();
  }

  public List<Follow> findAllById(Long memberId)
  {
    String sql = """
        SELECT * FROM FOLLOW WHERE toMemberId=:memberId
        """;

    RowMapper<Follow> rowMapper = (rs, rnum) -> Follow.builder()
        .id(rs.getLong("id"))
        .fromMemberId(rs.getLong("fromMemberId"))
        .toMemberId(rs.getLong("toMemberId"))
        .createdAt(rs.getObject("createdAt", LocalDateTime.class))
        .build();

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("memberId", memberId);

    return namedParameterJdbcTemplate.query(sql, parameterSource, rowMapper);
  }
}
