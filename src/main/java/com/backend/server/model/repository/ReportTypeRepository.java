package com.backend.server.model.repository;

import com.backend.server.api.admin.dto.category.AdminCommonCategoryResponse;
import com.backend.server.model.entity.ReportType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportTypeRepository extends JpaRepository<ReportType, Long> {

    @Query("SELECT new com.backend.server.api.admin.dto.category.AdminCommonCategoryResponse" +
            "(r.id, r.name, r.description, COUNT(rt), r.createdAt) " +
            "FROM ReportType r LEFT JOIN r.reports rt " +
            "GROUP BY r.id")
    List<AdminCommonCategoryResponse> getReportTypeList();
}