package com.backend.server.api.user.equipment.dto.equipment;

import com.backend.server.api.common.dto.PageableRequest;

import lombok.Getter;

@Getter
public class EquipmentListRequest implements PageableRequest{
    private String modelName;
    private String renterName;
    private Long categoryId;
    private Boolean isAvailable;
    private String searchKeyword;

    //
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
    // Getter
    @Override public Integer getPage() { return page; }
    @Override public Integer getSize() { return size; }
    @Override public String getSortBy() { return sortBy; }
    @Override public String getSortDirection() { return sortDirection; }

}
