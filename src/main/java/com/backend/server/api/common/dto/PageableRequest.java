package com.backend.server.api.common.dto;

import lombok.Getter;

public interface PageableRequest {
    Integer getPage();
    Integer getSize();
    String getSortBy();
    String getSortDirection();
    
    
}