package com.example.fastcampusmysql.domain.dto;

import com.example.fastcampusmysql.domain.entity.Follow;

public record FollowDto(MemberDto fromMember, MemberDto toMember) {}
