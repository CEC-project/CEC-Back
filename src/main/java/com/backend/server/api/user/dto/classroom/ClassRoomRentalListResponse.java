package com.backend.server.api.user.dto.classroom;

import java.util.List;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.ClassRoomRental;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class ClassRoomRentalListResponse {
    private List<ClassRoomRentalResponse> content;
    private PageableInfo pageable;
    private List<FailedRentalInfo> failedRequests;
    
    public ClassRoomRentalListResponse(Page<ClassRoomRental> page) {
        this.content = page.getContent().stream().map(ClassRoomRentalResponse::new).toList();
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
        this.failedRequests = null;
    }
    
    public ClassRoomRentalListResponse(List<ClassRoomRentalResponse> responses) {
        this.content = responses;
        this.pageable = null;
        this.failedRequests = null;
    }
    
    public ClassRoomRentalListResponse(List<ClassRoomRentalResponse> responses, List<FailedRentalInfo> failedRequests) {
        this.content = responses;
        this.pageable = null;
        this.failedRequests = failedRequests;
    }
    
    @Getter
    @AllArgsConstructor
    public static class FailedRentalInfo {
        private Long classRoomId;
        private String reason;
    }
}
