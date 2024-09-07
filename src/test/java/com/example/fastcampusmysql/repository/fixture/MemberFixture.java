package com.example.fastcampusmysql.repository.fixture;

import com.example.fastcampusmysql.domain.entity.Member;
import java.time.LocalDate;
import java.util.Random;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.randomizers.text.StringRandomizer;

public class MemberFixture {

  static EasyRandom easyRandom;

  static {
    EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
        .excludeField(FieldPredicates.named("id"))
        .randomize(FieldPredicates.named("nickName"), new StringRandomizer(5,5, new Random().nextLong()))
        .randomize(FieldPredicates.named("email"), new StringRandomizer(10, 10, new Random().nextLong()))
        .dateRange(LocalDate.now().minusDays(1), LocalDate.now());

    easyRandom = new EasyRandom(easyRandomParameters);
  }

  public static Member create()
  {
    Member member = easyRandom.nextObject(Member.class);

    return Member.builder()
        .nickName(member.getNickName())
        .email(member.getEmail() + "@gmail.com")
        .birth(member.getBirth())
        .build();
  }

}
