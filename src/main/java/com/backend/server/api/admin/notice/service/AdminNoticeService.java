package com.backend.server.api.admin.notice.service;

import com.backend.server.api.admin.notice.dto.AdminNoticeCreateRequest;
import com.backend.server.api.admin.notice.dto.AdminNoticeIdResponse;
import com.backend.server.api.admin.notice.dto.AdminNoticeListRequest;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.notice.dto.NoticeListResponse;
import com.backend.server.api.user.notice.dto.NoticeResponse;
import com.backend.server.model.entity.Notice;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.NoticeRepository;
import com.backend.server.model.repository.NoticeSpecification;
import com.backend.server.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 관리자용 공지사항 서비스
 * <p>
 * 공지사항의 생성, 수정, 삭제 등 관리자 기능을 처리하는 서비스 클래스입니다.
 * 모든 메소드는 트랜잭션 내에서 실행됩니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdminNoticeService {

  private final NoticeRepository noticeRepository;
  private final UserRepository userRepository;

  /**
   * 공지사항을 생성합니다.
   * <p>
   * 동일한 제목의 공지사항이 이미 존재할 경우 예외가 발생합니다.
   * 로그인한 관리자가 공지사항의 작성자로 설정됩니다.
   *
   * @param request   공지사항 생성 요청 정보
   * @param loginUser 로그인한 관리자 정보
   * @return 생성된 공지사항의 ID를 담은 응답 객체
   * @throws RuntimeException 동일한 제목의 공지사항이 이미 존재하거나 관리자를 찾을 수 없는 경우
   */
  public Long createNotice(AdminNoticeCreateRequest request, LoginUser loginUser) {
    if (noticeRepository.existsByTitle(request.getTitle())) {
      throw new RuntimeException("공지사항 제목 중복");
    }

    User author = userRepository.findById(loginUser.getId())
        .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));

    Notice notice = noticeRepository.save(request.toEntity(author));
    return notice.getId();
  }

  /**
   * 공지사항 목록을 조회합니다.
   *
   * 요청에 포함된 필터 조건과 페이징 조건에 맞게 공지사항 목록을 조회합니다.
   * 필터링은 NoticeSpecification을 통해 처리됩니다.
   *
   * @param request 공지사항 목록 조회 요청 정보 (필터 조건, 페이징 정보 포함)
   * @return 조회된 공지사항 목록과 페이징 정보를 담은 응답 객체
   */
  public NoticeListResponse getNotices(AdminNoticeListRequest request) {
    Specification<Notice> spec = NoticeSpecification.filterNotices(request);
    Page<Notice> page = noticeRepository.findAll(spec, request.toPageable());
    return new NoticeListResponse(page);
  }

  /**
   * 특정 ID의 공지사항을 조회합니다.
   *
   * 지정된 ID에 해당하는 공지사항을 데이터베이스에서 찾아 반환합니다.
   * 존재하지 않는 ID를 지정할 경우 예외가 발생합니다.
   *
   * @param noticeId 조회할 공지사항의 ID
   * @return 조회된 공지사항 정보를 담은 응답 객체
   * @throws IllegalArgumentException 조회하려는 공지사항이 존재하지 않는 경우
   */
  public NoticeResponse getNotice(Long noticeId) {
    Notice notice = noticeRepository.findById(noticeId)
        .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

    return new NoticeResponse(notice);
  }

  /**
   * 공지사항을 수정합니다.
   * <p>
   * 기존 공지사항의 제목, 내용, 첨부파일 URL, 중요도를 수정합니다.
   * 수정하려는 공지사항이 존재하지 않을 경우 예외가 발생합니다.
   *
   * @param id      수정할 공지사항의 ID
   * @param request 공지사항 수정 요청 정보
   * @return 수정된 공지사항의 ID를 담은 응답 객체
   * @throws RuntimeException 수정하려는 공지사항을 찾을 수 없는 경우
   */
  public Long updateNotice(Long id, AdminNoticeCreateRequest request) {
    Notice notice = noticeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

    Notice updated = notice.toBuilder()
        .title(request.getTitle())
        .content(request.getContent())
        .attachmentUrl(request.getAttachmentUrl())
        .important(request.getImportant())
        .build();

    updated = noticeRepository.save(updated);

    return updated.getId();
  }

  /**
   * 공지사항을 삭제합니다.
   * <p>
   * 지정된 ID의 공지사항을 데이터베이스에서 삭제합니다.
   * 존재하지 않는 ID를 지정할 경우 예외가 발생합니다.
   *
   * @param id 삭제할 공지사항의 ID
   * @return 삭제된 공지사항의 ID를 담은 응답 객체
   * @throws RuntimeException 삭제하려는 공지사항이 존재하지 않는 경우
   */
  public Long deleteNotice(Long id) {
    if (!noticeRepository.existsById(id)) {
      throw new RuntimeException("공지사항을 찾을 수 없습니다.");
    }

    noticeRepository.deleteById(id);

    return id;
  }
}