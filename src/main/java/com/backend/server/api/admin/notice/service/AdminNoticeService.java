package com.backend.server.api.admin.notice.service;

import com.backend.server.api.admin.notice.dto.AdminNoticeCreateRequest;
import com.backend.server.api.admin.notice.dto.AdminNoticeIdResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.entity.Notice;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.NoticeRepository;
import com.backend.server.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminNoticeService {

  private final NoticeRepository noticeRepository;
  private final UserRepository userRepository;

  public AdminNoticeIdResponse createNotice(AdminNoticeCreateRequest request, LoginUser loginUser) {
    if (noticeRepository.existsByTitle(request.title())) {
      throw new RuntimeException("공지사항 제목 중복");
    }

    User author = userRepository.findById(loginUser.getId())
        .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));

    Notice notice = noticeRepository.save(request.toEntity(author));
    return new AdminNoticeIdResponse(notice.getId());
  }
}

