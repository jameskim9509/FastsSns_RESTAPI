package com.example.fastcampusmysql.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@Builder
@ToString // for Test
public class Member {
  final Long id;
  final String email;
  String nickName;
  final LocalDate birth;
  final LocalDateTime createdAt;

  private final int NICKNAME_MAXLEN = 10;

  Member(Long id, String email, String nickName, LocalDate birth, LocalDateTime createdAt)
  {
    if(email==null || nickName==null || birth==null || !validateNickName(nickName))
      throw new IllegalArgumentException();

    this.id = id;
    this.email = email;
    this.nickName = nickName;
    this.birth = birth;
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
  }

  public void changeNickName(String nickName)
  {
    Assert.notNull(nickName, "");
    Assert.isTrue(validateNickName(nickName),"");

    this.nickName = nickName;
  }

  boolean validateNickName(String nickName)
  {
    return nickName.length() < NICKNAME_MAXLEN ? true : false;
  }
}
