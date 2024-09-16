package com.example.fastcampusmysql.service;

import com.example.fastcampusmysql.domain.entity.TimeLine;
import com.example.fastcampusmysql.repository.TimeLineRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimeLineService {
  final TimeLineRepository timeLineRepository;
  public void saveAllMemberIdsOfPostId(Long postId, List<Long> memberIds)
  {
    timeLineRepository.saveAllMemberIdsOfPostId(postId, memberIds);
  }

  public List<Long> getPostIdsByMemberId(Long memberId)
  {
    return timeLineRepository.getTimeLinesInMemberIds(memberId).stream()
        .map(TimeLine::getPostId)
        .collect(Collectors.toList());
  }
}
