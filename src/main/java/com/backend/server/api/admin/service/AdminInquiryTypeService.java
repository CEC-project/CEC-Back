package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.category.AdminCommonCategoryRequest;
import com.backend.server.api.admin.dto.category.AdminCommonCategoryResponse;
import com.backend.server.model.entity.InquiryType;
import com.backend.server.model.repository.InquiryTypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminInquiryTypeService {
    private final InquiryTypeRepository inquiryTypeRepository;

    public List<AdminCommonCategoryResponse> getInquiryTypeList() {
        return inquiryTypeRepository.getInquiryTypeList();
    }

    public Long createInquiryType(AdminCommonCategoryRequest request) {
        InquiryType inquiryType = InquiryType.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return inquiryTypeRepository.save(inquiryType).getId();
    }

    @Transactional
    public Long updateInquiryType(Long id, AdminCommonCategoryRequest request) {
        InquiryType inquiryType = inquiryTypeRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        inquiryType.toBuilder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        inquiryTypeRepository.save(inquiryType);
        return inquiryType.getId();
    }

    public void deleteInquiryType(Long id) {
        inquiryTypeRepository.deleteById(id);
    }
}