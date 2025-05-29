package com.backend.server.api.user.notice.service;

import com.backend.server.api.admin.notice.dto.AdminNoticeListRequest;
import com.backend.server.api.user.notice.dto.NoticeListResponse;
import com.backend.server.api.user.notice.dto.NoticeResponse;
import com.backend.server.model.entity.Notice;
import com.backend.server.model.repository.NoticeRepository;
import com.backend.server.model.repository.NoticeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;

    /**
     * 공지사항 목록을 조회합니다.
     * <p>
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
     * <p>
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
        notice.increaseViewCount();

        return new NoticeResponse(notice);
    }
}
