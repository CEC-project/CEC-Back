package com.backend.server.api.admin.inquiry.service;

import com.backend.server.api.admin.inquiry.dto.AdminInquiryAnswerRequest;
import com.backend.server.api.admin.inquiry.dto.AdminInquiryListRequest;
import com.backend.server.api.admin.inquiry.dto.AdminInquiryListResponse;
import com.backend.server.api.common.notification.service.CommonNotificationService;
import com.backend.server.model.entity.Inquiry;
import com.backend.server.model.entity.InquiryAnswer;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.inquiry.InquiryAnswerRepository;
import com.backend.server.model.repository.inquiry.InquiryRepository;
import com.backend.server.model.repository.inquiry.InquirySpecification;
import com.backend.server.model.repository.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminInquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryAnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final CommonNotificationService notificationService;

    @Transactional(readOnly = true)
    public AdminInquiryListResponse getInquiries(AdminInquiryListRequest request) {
        Pageable pageable = request.toPageable();
        Specification<Inquiry> spec = InquirySpecification.filterInquiry(request);
        Page<Inquiry> inquiries = inquiryRepository.findAll(spec, pageable);

        List<List<InquiryAnswer>> answers = inquiries
                .getContent()
                .stream()
                .map(Inquiry::getAnswers)
                .toList();
        List<List<User>> responders = answers
                .stream()
                .map((answer) -> answer
                        .stream()
                        .map(InquiryAnswer::getResponder)
                        .toList())
                .toList();
        List<User> users = inquiries
                .getContent()
                .stream()
                .map(Inquiry::getAuthor)
                .toList();
        List<Professor> professors = users
                .stream()
                .map(User::getProfessor)
                .toList();

        return new AdminInquiryListResponse(inquiries, answers, responders, users, professors);
    }

    public Long addResponse(Long inquiryId, AdminInquiryAnswerRequest request, Long responderId) {
        User responder = userRepository.findById(responderId)
                .orElseThrow(() -> new IllegalArgumentException("현재 로그인한 사용자가 DB 에서 찾아지지 않음"));
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("답변할 문의의 id가 DB 에서 찾아지지 않음"));
        InquiryAnswer answer = request.toEntity(responder, inquiry);
        answerRepository.save(answer);

        // 알림: 문의 작성자에게 답변 알림 발송
        notificationService.notificationProcess(
                inquiry.getAuthor().getId(), // 대상(문의 작성자)
                "문의답변", // 카테고리
                "문의 [" + inquiry.getTitle() + "]에 답변이 등록되었습니다.", // 타이틀
                responder.getNickname() + " 관리자가 답변을 남겼습니다.", // 메시지
                "/inquiry/" + inquiry.getId() // 링크(문의 상세)
        );

        return answer.getId();
    }
}