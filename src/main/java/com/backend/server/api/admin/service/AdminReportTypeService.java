package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.category.AdminCommonCategoryRequest;
import com.backend.server.api.admin.dto.category.AdminCommonCategoryResponse;
import com.backend.server.model.entity.ReportType;
import com.backend.server.model.repository.ReportTypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminReportTypeService {
    private final ReportTypeRepository reportTypeRepository;

    public List<AdminCommonCategoryResponse> getReportTypeList() {
        return reportTypeRepository.getReportTypeList();
    }

    public Long createReportType(AdminCommonCategoryRequest request) {
        ReportType reportType = ReportType.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return reportTypeRepository.save(reportType).getId();
    }

    @Transactional
    public Long updateReportType(Long id, AdminCommonCategoryRequest request) {
        ReportType reportType = reportTypeRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        reportType.toBuilder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        reportTypeRepository.save(reportType);
        return reportType.getId();
    }

    public void deleteReportType(Long id) {
        reportTypeRepository.deleteById(id);
    }
}