package com.example.fastcampusmysql.domain.dto.Input;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record PostCountInput(
    Long memberId,
    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
}
