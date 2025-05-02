package com.backend.server.api.user.dto.classroom;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.ClassRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteListResponse {
    private List<ClassRoomResponse> content;
    private PageableInfo pageable;
    
    
    public FavoriteListResponse(Page<ClassRoom> page) {
        this.content = page.getContent().stream()
                .map(ClassRoomResponse::new)
                .collect(Collectors.toList());
        this.pageable = new PageableInfo(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
