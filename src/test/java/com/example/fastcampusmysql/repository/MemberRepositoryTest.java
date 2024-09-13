package com.example.fastcampusmysql.repository;

import com.example.fastcampusmysql.domain.entity.Member;
import com.example.fastcampusmysql.repository.fixture.MemberFixture;
import java.util.stream.IntStream;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

class MemberRepositoryTest {

  EasyRandom easyRandom = new EasyRandom();

  @Test
  void save() {
    IntStream.range(0, 10)
        .mapToObj(i -> MemberFixture.create())
        .forEach(m -> System.out.println(m.toString()));
  }

  @Test
  void findMemberById() {
  }

  @Test
  void changeNickName() {
  }
}