package com.example.fastcampusmysql.domain.dto.Input;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record RegsiterInput(String email, String nickName, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birth) {}
