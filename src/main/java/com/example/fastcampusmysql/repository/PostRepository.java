package com.example.fastcampusmysql.repository;

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

  public Optional<Post> update(Post post)
  {
    String sql = """
        UPDATE Post
        SET memberId=:memberId,
            contents=:contents,
            createdDate=:createdDate,
            likeCount=:likeCount, 
            version=:version+1 
        WHERE id=:id AND version=:version
        """;

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("memberId", post.getMemberId())
        .addValue("contents", post.getContents())
        .addValue("createdDate", post.getCreatedDate())
        .addValue("likeCount", post.getLikeCount())
        .addValue("version", post.getVersion())
        .addValue("id", post.getId());

    int rowNum = namedParameterJdbcTemplate.update(sql, parameterSource);

    if(rowNum > 0)
      return Optional.of(
          Post.builder()
            .id(post.getId())
            .memberId(post.getMemberId())
            .contents(post.getContents())
            .createdDate(post.getCreatedDate())
            .createdAt(post.getCreatedAt())
            .likeCount(post.getLikeCount())
            .build()
      );
    else return Optional.ofNullable(null);
  }

  public Post save(Long memberId, String contents)
  {
    Post post = Post.builder()
        .memberId(memberId)
        .contents(contents)
        .build();

    String sql = """
        INSERT INTO 
        POST(memberId, contents, createdDate, createdAt, likeCount, version) 
        VALUES(:memberId, :contents, :createdDate, :createdAt, :likeCount, :version)
        """;

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("memberId", post.getMemberId())
        .addValue("contents", post.getContents())
        .addValue("createdDate", post.getCreatedDate())
        .addValue("createdAt", post.getCreatedAt())
        .addValue("likeCount", post.getLikeCount())
        .addValue("version", post.getVersion());

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, parameterSource, keyHolder);

    return Post.builder()
        .id(keyHolder.getKey().longValue())
        .memberId(post.getMemberId())
        .contents(post.getContents())
        .createdDate(post.getCreatedDate())
        .createdAt(post.getCreatedAt())
        .likeCount(post.getLikeCount())
        .version(post.getVersion())
        .build();
  }

  // for Test
  public void saveAll(List<Post> posts)
  {
    String sql = """
        INSERT INTO 
        POST(memberId, contents, createdDate, createdAt, likeCount, version) 
        VALUES(:memberId, :contents, :createdDate, :createdAt, :likeCount, :version)
        """;

    MapSqlParameterSource[] batchArgs = posts.stream()
        .map(p -> new MapSqlParameterSource()
                .addValue("memberId", p.getMemberId())
                .addValue("contents", p.getContents())
                .addValue("createdDate", p.getCreatedDate())
                .addValue("createdAt", p.getCreatedAt())
                .addValue("likeCount", p.getLikeCount())
                .addValue("version", p.getVersion())
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
            .likeCount(rs.getLong("likeCount"))
            .version(rs.getLong("version"))
            .build();

    return Optional.ofNullable(
        namedParameterJdbcTemplate.query(sql, parameterSource, rowMapper)
    );
  }

  public Optional<Post> getPostById(Long id, Boolean isWriteLock)
  {
    String sql =
        """
        SELECT *
        FROM Post
        WHERE id=:id 
        """;

    if(isWriteLock) sql += "FOR UPDATE";

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("id", id);

    RowMapper<Post> rowMapper = (rs, rnum) ->
        Post.builder()
            .id(rs.getLong("id"))
            .memberId(rs.getLong("memberId"))
            .contents(rs.getString("contents"))
            .createdDate(rs.getObject("createdDate", LocalDate.class))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .likeCount(rs.getLong("likeCount"))
            .version(rs.getLong("version"))
            .build();

    return Optional.ofNullable(
        namedParameterJdbcTemplate.queryForObject(sql, parameterSource, rowMapper)
    );
  }

  public List<Post> getPostsByIdsOrderByIdDESC(List<Long> ids)
  {
    String sql = """
        SELECT *
        FROM Post
        WHERE id in (:ids)
        ORDER BY id DESC
        """;

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("ids", ids);

    RowMapper<Post> rowMapper = (rs, rnum) ->
        Post.builder()
            .id(rs.getLong("id"))
            .memberId(rs.getLong("memberId"))
            .contents(rs.getString("contents"))
            .createdDate(rs.getObject("createdDate", LocalDate.class))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .likeCount(rs.getLong("likeCount"))
            .version(rs.getLong("version"))
            .build();

    return namedParameterJdbcTemplate.query(sql, parameterSource, rowMapper);
  }

  public List<Post> getPostsByMemberIds(List<Long> memberIds)
  {
    String sql = """
        SELECT *
        FROM Post
        WHERE memberId in (:memberIds)
        """;

    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("memberIds", memberIds);

    RowMapper<Post> rowMapper = (rs, rnum) ->
        Post.builder()
            .id(rs.getLong("id"))
            .memberId(rs.getLong("memberId"))
            .contents(rs.getString("contents"))
            .createdDate(rs.getObject("createdDate", LocalDate.class))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .likeCount(rs.getLong("likeCount"))
            .version(rs.getLong("version"))
            .build();

    return namedParameterJdbcTemplate.query(sql, parameterSource, rowMapper);
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
            .likeCount(rs.getLong("likeCount"))
            .version(rs.getLong("version"))
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
    String sql = String.format(
        """
        SELECT *
        FROM Post
        WHERE memberId=:memberId
        ORDER BY %s
        LIMIT :size
        OFFSET :offset
        """,
        sort
    );

    // covering index
//    String sql = String.format(
//          """
//          SELECT *
//          FROM (SELECT id
//                FROM Post
//                WHERE memberId=:memberId
//                ORDER BY id DESC
//                LIMIT :size
//                OFFSET :offset) as temp
//          JOIN Post as p
//          ON p.id = temp.id
//        """
//    );

    // String을 mapping하면 '~'으로 들어감 주의
    SqlParameterSource parameterSource = new MapSqlParameterSource()
        .addValue("memberId", memberId)
        .addValue("offset", pageable.getOffset())
        .addValue("size", pageable.getPageSize());

    RowMapper<Post> rowMapper = (rs, rnum) ->
        Post.builder()
            .id(rs.getLong("id"))
            .memberId( rs.getLong("memberId"))
            .contents(rs.getString("contents"))
            .createdDate(rs.getObject("createdDate", LocalDate.class))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .likeCount(rs.getLong("likeCount"))
            .version(rs.getLong("version"))
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
