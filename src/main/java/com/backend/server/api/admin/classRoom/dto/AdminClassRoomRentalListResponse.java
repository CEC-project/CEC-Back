package com.backend.server.api.admin.classRoom.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminClassRoomRentalListResponse {
    private String status;               // 
    private String searchKeyword;    // )
    private Integer searchType;      
    private LocalDateTime startDate;   
    private LocalDateTime endDate;      
    private String sortBy;       
    private Integer page;             
    private Integer size;    
}
