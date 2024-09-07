package com.example.fastcampusmysql.repository;

import com.example.fastcampusmysql.domain.entity.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class MemberRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  MemberRepository(DataSource dataSource)
  {
    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  public Member save(Member member)
  {
    String sql = "INSERT INTO MEMBER(email, nickName, birth, createdAt) VALUES(:email, :nickName, :birth, :createdAt)";
    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("email", member.getEmail())
        .addValue("nickName", member.getNickName())
        .addValue("birth", member.getBirth())
        .addValue("createdAt", member.getCreatedAt());

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder);
    return Member.builder()
        .id(keyHolder.getKey().longValue())
        .nickName(member.getNickName())
        .email(member.getEmail())
        .birth(member.getBirth())
        .createdAt(member.getCreatedAt())
        .build();
  }

  public Optional<Member> findMemberById(Long id)
  {
    String sql = "SELECT * FROM MEMBER WHERE id=:id";
    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("id", id);

    RowMapper<Member> rowMapper = (rs, rnum) -> Member.builder()
        .id(rs.getLong("id"))
        .email(rs.getString("email"))
        .nickName(rs.getString("nickName"))
        .birth(rs.getObject("birth", LocalDate.class))
        .createdAt(rs.getObject("createdAt", LocalDateTime.class))
        .build();

    Member member = namedParameterJdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
    return Optional.ofNullable(member);
  }

  public Member changeNickName(Long id, String nickName)
  {
    Member member = findMemberById(id).orElseThrow(() -> new IllegalArgumentException());

    String sql = "UPDATE MEMBER SET nickName=:nickName WHERE id=:id";
    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("nickName", nickName)
        .addValue("id", member.getId());

    namedParameterJdbcTemplate.update(sql, parameterSource);

    return Member.builder()
        .id(member.getId())
        .nickName(nickName)
        .email(member.getEmail())
        .birth(member.getBirth())
        .createdAt(member.getCreatedAt())
        .build();
  }
}
