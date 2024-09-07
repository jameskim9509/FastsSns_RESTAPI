package com.example.fastcampusmysql.repository;

import com.example.fastcampusmysql.domain.entity.MemberNickNameHistory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberNickNameHistoryRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  MemberNickNameHistoryRepository(DataSource dataSource)
  {
    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  public MemberNickNameHistory save(Long memberId, String nickName)
  {
    MemberNickNameHistory memberNickNameHistory =
        MemberNickNameHistory.builder()
            .memberId(memberId)
            .nickName(nickName)
            .build();

    // JAVA 17 TextBlock
    String sql = """
        INSERT INTO MemberNickNameHistory(memberId, nickName, createdAt) VALUES(:memberId, :nickName, :createdAt)
        """;
    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("memberId", memberId)
        .addValue("nickName", nickName)
        .addValue("createdAt", memberNickNameHistory.getCreatedAt());

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder);

    return MemberNickNameHistory.builder()
        .id(keyHolder.getKey().longValue())
        .memberId(memberNickNameHistory.getMemberId())
        .nickName(memberNickNameHistory.getNickName())
        .createdAt(memberNickNameHistory.getCreatedAt())
        .build();
  }
}
