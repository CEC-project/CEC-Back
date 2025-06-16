package com.backend.server.model.repository.history;

import com.backend.server.api.user.history.dto.RentalHistoryListRequest;
import com.backend.server.model.entity.RentalHistory;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.entity.equipment.EquipmentModel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class RentalHistorySpecification {
    public static Specification<RentalHistory> filter(RentalHistoryListRequest request) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            // 상태 필터링
            if (request.getStartDate() != null)
                predicate = cb.and(predicate, cb.equal(root.get("status"), request.getStatus()));

            // 날짜 조건
            if (request.getStartDate() != null)
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        request.getStartDate().atStartOfDay()));

            if (request.getEndDate() != null)
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(
                        root.get("createdAt"),
                        request.getEndDate().atTime(23, 59, 59)));

            // 검색 키워드
            if (request.getSearchType() != null
                    && request.getSearchKeyword() != null
                    && !request.getSearchKeyword().isBlank()) {
                String keyword = "%" + request.getSearchKeyword().toLowerCase() + "%";

                Join<RentalHistory, Equipment> equipment = root.join("equipment", JoinType.LEFT);
                Join<Equipment, EquipmentModel> model = equipment.join("equipmentModel", JoinType.LEFT);
                Join<EquipmentModel, EquipmentCategory> category = model.join("category", JoinType.LEFT);
                Join<RentalHistory, Classroom> classroom = root.join("classroom", JoinType.LEFT);

                Predicate id = cb.equal(root.get("id"), Long.parseLong(request.getSearchKeyword()));
                Predicate classroomName = cb.equal(classroom.get("name"), keyword);
                Predicate modelName = cb.equal(model.get("name"), keyword);
                Predicate categoryName = cb.equal(category.get("name"), keyword);
                Predicate serialNumber = cb.equal(equipment.get("serialNumber"), keyword);
                Predicate all = cb.and(predicate, id, classroomName, modelName, categoryName, serialNumber);

                Predicate newPredicate = switch (request.getSearchType()) {
                    case ID -> id;
                    case CLASSROOM_NAME -> classroomName;
                    case EQUIPMENT_MODEL_NAME -> modelName;
                    case EQUIPMENT_CATEGORY_NAME -> categoryName;
                    case EQUIPMENT_SERIAL_NUMBER -> serialNumber;
                    case ALL -> all;
                };
                predicate = cb.and(predicate, newPredicate);
            }

            return predicate;
        };
    }
}
