package com.backend.server.api.user.inquiry.service;


import com.backend.server.api.user.inquiry.dto.InquiryRequest;
import com.backend.server.api.user.inquiry.dto.InquiryResponse;
import com.backend.server.model.entity.Inquiry;

import com.backend.server.model.repository.InquiryRepository;


import java.nio.file.AccessDeniedException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;


    @Transactional
    public Long createInquiry(InquiryRequest request, Long currentUserId) { // 문의 글 쓰기


        Inquiry inquiry = Inquiry.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .attachmentUrl(request.getAttachmentUrl())
                .authorId(currentUserId)
                .type(request.getType())
                .build();

        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        return savedInquiry.getId();
    }

    @Transactional(readOnly = true)
    public InquiryResponse getInquiry(Long id, Long currentUserId) throws AccessDeniedException { // 게시글 상세 조회
        Inquiry inquiry = inquiryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 문의글이 존재하지 않습니다."));

        if (!inquiry.getAuthorId().equals(currentUserId)){
            throw new AccessDeniedException("본인의 문의글만 조회할 수 있습니다.");
        }

        return InquiryResponse.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .attachmentUrl(inquiry.getAttachmentUrl())
                .type(inquiry.getType())
                .status(inquiry.getStatus())
                .createdAt(inquiry.getCreatedAt().toString())
                .build();
    }

    @Transactional(readOnly = true)
    public List<InquiryResponse> getMyInquiries(Long currentUserId) { // 내 글 전체 조회
        return inquiryRepository.findAllbyAuthorId(currentUserId).stream()
                .map(inquiry -> InquiryResponse.builder()
                        .id(inquiry.getId())
                        .title(inquiry.getTitle())
                        .content(inquiry.getContent())
                        .attachmentUrl(inquiry.getAttachmentUrl())
                        .type(inquiry.getType())
                        .status(inquiry.getStatus())
                        .createdAt(inquiry.getCreatedAt().toString())
                        .build())
        .toList();
    }
}