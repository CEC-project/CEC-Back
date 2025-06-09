package com.backend.server.api.user.inquiry.service;

import com.backend.server.api.user.inquiry.dto.*;
import com.backend.server.model.entity.Inquiry;
import com.backend.server.model.entity.InquiryAnswer;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.InquiryRepository;
import com.backend.server.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    private String joinAttachments(List<String> attachments) {
        return (attachments != null && !attachments.isEmpty()) ? String.join(",", attachments) : null;
    }

    private List<String> splitAttachments(String attachmentUrl) {
        return (attachmentUrl != null && !attachmentUrl.isBlank()) ? Arrays.asList(attachmentUrl.split(",")) : Collections.emptyList();
    }

    private InquiryAnswerResponse mapAnswer(InquiryAnswer answer) {
        if (answer == null) return null;
        User responder = answer.getResponder();
        return InquiryAnswerResponse.builder()
                .content(answer.getContent())
                .author(AuthorResponse.builder()
                        .id(responder.getId())
                        .nickname(responder.getNickname())
                        .role(responder.getRole().name())
                        .imageUrl(responder.getProfilePicture())
                        .build())
                .build();
    }

    @Transactional
    public Long createInquiry(InquiryRequest request, Long currentUserId) {
        User author = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("로그인된 사용자가 DB에 없음"));

        Inquiry inquiry = Inquiry.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .attachmentUrl(joinAttachments(request.getAttachments()))
                .author(author)
                .type(request.getType())
                .build();

        Inquiry saved = inquiryRepository.save(inquiry);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public InquiryResponse getInquiry(Long id, Long currentUserId) throws AccessDeniedException {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의글이 존재하지 않습니다."));

        if (!inquiry.getAuthor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("본인의 문의글만 조회할 수 있습니다.");
        }

        Optional<InquiryAnswer> answerOpt = inquiry.getAnswers().stream().findFirst();

        return InquiryResponse.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .attachments(splitAttachments(inquiry.getAttachmentUrl()))
                .type(inquiry.getType())
                .status(inquiry.getStatus())
                .createdAt(inquiry.getCreatedAt().toString())
                .answer(answerOpt.map(this::mapAnswer).orElse(null))
                .build();
    }

    @Transactional(readOnly = true)
    public Page<InquiryResponse> getMyInquiries(Long currentUserId, int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection).orElse(Sort.Direction.DESC);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortBy));

        Page<Inquiry> inquiries = inquiryRepository.findAllByAuthorId(currentUserId, pageable);

        return inquiries.map(inquiry -> {
            Optional<InquiryAnswer> answerOpt = inquiry.getAnswers().stream().findFirst();
            return InquiryResponse.builder()
                    .id(inquiry.getId())
                    .title(inquiry.getTitle())
                    .content(inquiry.getContent())
                    .attachments(splitAttachments(inquiry.getAttachmentUrl()))
                    .type(inquiry.getType())
                    .status(inquiry.getStatus())
                    .createdAt(inquiry.getCreatedAt().toString())
                    .answer(answerOpt.map(this::mapAnswer).orElse(null))
                    .build();
        });
    }

    @Transactional
    public void updateInquiry(Long id, InquiryRequest request, Long currentUserId) throws AccessDeniedException {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의글이 존재하지 않습니다."));

        if (!inquiry.getAuthor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("본인의 문의글만 수정할 수 있습니다.");
        }

        inquiry.update(
                request.getTitle(),
                request.getContent(),
                joinAttachments(request.getAttachments()),
                request.getType()
        );
    }

    @Transactional
    public void deleteInquiry(Long id, Long currentUserId) throws AccessDeniedException {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의글이 존재하지 않습니다."));

        if (!inquiry.getAuthor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("본인의 문의글만 삭제할 수 있습니다.");
        }

        inquiryRepository.delete(inquiry);
    }
}
