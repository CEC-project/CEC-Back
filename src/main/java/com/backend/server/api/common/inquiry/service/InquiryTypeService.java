package com.backend.server.api.common.inquiry.service;

import com.backend.server.api.common.inquiry.dto.InquiryTypeResponse;
import com.backend.server.model.entity.InquiryType;
import com.backend.server.model.repository.InquiryTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryTypeService {

    private final InquiryTypeRepository inquiryTypeRepository;

    public List<InquiryTypeResponse> getAllInquiryTypes() {
        List<InquiryType> inquiryTypes = inquiryTypeRepository.findAll();

        return inquiryTypes.stream()
                .map(it -> InquiryTypeResponse.builder()
                        .id(it.getId())
                        .name(it.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
