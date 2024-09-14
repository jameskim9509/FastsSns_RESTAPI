package com.example.fastcampusmysql.repository;

import com.example.fastcampusmysql.domain.dto.CursorPostDto;
import com.example.fastcampusmysql.domain.dto.PostCountDto;
import com.example.fastcampusmysql.domain.entity.Post;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
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

  // for Test
  public void saveAll(List<Post> posts)
  {
    String sql = """
        INSERT INTO POST(memberId, contents, createdDate, createdAt) VALUES(:memberId, :contents, :createdDate, :createdAt)
        """;

    MapSqlParameterSource[] batchArgs = posts.stream()
        .map(p -> new MapSqlParameterSource()
                .addValue("memberId", p.getMemberId())
                .addValue("contents", p.getContents())
                .addValue("createdDate", p.getCreatedDate())
                .addValue("createdAt", p.getCreatedAt())
        ).toArray(MapSqlParameterSource[]::new);

    namedParameterJdbcTemplate.batchUpdate(sql, batchArgs);
  }

  //for Test
  public Optional<List<Post>> postQuery(String sql, SqlParameterSource parameterSource)
  {
    RowMapper<Post> rowMapper = (rs, rnum) ->
        Post.builder()
            .id(rs.getLong("id"))
            .memberId( rs.getLong("memberId"))
            .contents(rs.getString("contents"))
            .createdDate(rs.getObject("createdDate", LocalDate.class))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .build();

    return Optional.ofNullable(
        namedParameterJdbcTemplate.query(sql, parameterSource, rowMapper)
    );
  }

  public List<Post> getPostsByCursor(Long memberId, Integer key, Integer size)
  {
    String sql = "";
    if(key == null) {
      sql = """
          SELECT *
          FROM Post
          WHERE memberId=:memberId
          ORDER BY id DESC
          LIMIT :size
          """;
    } else {
      sql = """
          SELECT *
          FROM Post
          WHERE memberId=:memberId AND id<:key
          ORDER BY id DESC
          LIMIT :size
          """;
    }

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("memberId", memberId)
        .addValue("key", key)
        .addValue("size", size);

    RowMapper<Post> rowMapper = (rs, rnum) ->
        Post.builder()
            .id(rs.getLong("id"))
            .memberId(rs.getLong("memberId"))
            .contents(rs.getString("contents"))
            .createdDate(rs.getObject("createdDate", LocalDate.class))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .build();

    return namedParameterJdbcTemplate.query(sql, parameterSource, rowMapper);
  }

  public PageImpl<Post> getPostsByPage(Long memberId, Pageable pageable)
  {
    String sort = "";
    if(pageable.getSort().isEmpty()) sort = "id DESC";
    else {
      Iterator<Order> orderIterator = pageable.getSort().iterator();
      List<String> orderStringList = new ArrayList<>();
      while (orderIterator.hasNext()) {
        Order order = orderIterator.next();
        orderStringList.add(order.getProperty() + " " + order.getDirection());
      }
      sort = String.join(", ", orderStringList);
    }

    // ordinary pagination sql query
//    String sql = String.format(
//        """
//        SELECT *
//        FROM Post
//        WHERE memberId=:memberId
//        ORDER BY %s
//        LIMIT :size
//        OFFSET :offset
//        """,
//        sort
//    );

    // covering index
    String sql = String.format(
          """
          SELECT *
          FROM (SELECT id
                FROM Post
                WHERE memberId=:memberId
                LIMIT :size
                ORDER BY id DESC
                OFFSET :offset) as temp
          JOIN Post as p
          ON p.id = temp.id
        """,
        sort
    );

    System.out.println(sort);

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("memberId", memberId)
        .addValue("sort", sort)
        .addValue("offset", pageable.getOffset())
        .addValue("size", pageable.getPageSize());

    RowMapper<Post> rowMapper = (rs, rnum) ->
        Post.builder()
            .id(rs.getLong("id"))
            .memberId( rs.getLong("memberId"))
            .contents(rs.getString("contents"))
            .createdDate(rs.getObject("createdDate", LocalDate.class))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .build();

    List<Post> posts = namedParameterJdbcTemplate.query(sql, parameterSource, rowMapper);
    return new PageImpl(
        posts,
        pageable,
        getAllCountById(memberId)
    );
  }

  public Long getAllCountById(Long memberId)
  {
    String sql = """
        SELECT count(*) as count
        FROM Post
        WHERE memberId=:memberId
        """;

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("memberId", memberId);

    RowMapper<Long> rowMapper = (rs, rnum) -> rs.getLong("count");

    return namedParameterJdbcTemplate.queryForObject(sql, parameterSource, rowMapper);
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
