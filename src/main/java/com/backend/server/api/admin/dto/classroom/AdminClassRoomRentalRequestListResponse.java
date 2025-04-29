package com.backend.server.api.admin.dto.classroom;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.ClassRoom;
import com.backend.server.model.entity.ClassRoomRental;
import com.backend.server.model.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminClassRoomRentalRequestListResponse {
    private List<AdminClassRoomRentalRequestResponse> content;
    private PageableInfo pageable;

  

    //여기서 조립..
    public AdminClassRoomRentalRequestListResponse(
        Page<ClassRoomRental> page,
        Map<Long, User> userMap,
        Map<Long, ClassRoom> classRoomMap
    ) {
        this.content = page.getContent().stream()
            .map(rental -> new AdminClassRoomRentalRequestResponse(
                rental,
                classRoomMap.get(rental.getClassRoomId()),
                userMap.get(rental.getRenterId())
            ))
            .toList();

        this.pageable = new PageableInfo(
            page.getNumber(),
            page.getSize(),
            page.getTotalPages(),
            page.getTotalElements()
        );
    }
}
