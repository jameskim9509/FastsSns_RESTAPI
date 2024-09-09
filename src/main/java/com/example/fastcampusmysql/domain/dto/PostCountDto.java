package com.example.fastcampusmysql.domain.dto;

import java.time.LocalDate;

public record PostCountDto(Long memberId, LocalDate createdDate, Long PostCount) {

}
