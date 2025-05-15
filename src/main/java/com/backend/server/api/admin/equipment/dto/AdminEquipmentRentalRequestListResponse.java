// package com.backend.server.api.admin.dto.equipment;

// import java.util.List;

// import org.springframework.data.domain.Page;
// import com.backend.server.model.entity.EquipmentRental;
// import com.backend.server.model.entity.Equipment;
// import com.backend.server.model.entity.User;
// import java.util.Map;
// import com.backend.server.api.common.dto.PageableInfo;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// public class AdminEquipmentRentalRequestListResponse {
//     private List<AdminEquipmentRentalRequestResponse> content;
//     private PageableInfo pageable;

  

//     //여기서 조립..
//     public AdminEquipmentRentalRequestListResponse(
//         Page<EquipmentRental> page,
//         Map<Long, User> userMap,
//         Map<Long, Equipment> equipmentMap
//     ) {
//         this.content = page.getContent().stream()
//             .map(rental -> new AdminEquipmentRentalRequestResponse(
//                 rental,
//                 equipmentMap.get(rental.getEquipmentId()),
//                 userMap.get(rental.getUserId())
//             ))
//             .toList();

//         this.pageable = new PageableInfo(
//             page.getNumber(),
//             page.getSize(),
//             page.getTotalPages(),
//             page.getTotalElements()
//         );
//     }
// }