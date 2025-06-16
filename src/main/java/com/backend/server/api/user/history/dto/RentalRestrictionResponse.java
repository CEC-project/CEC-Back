package com.backend.server.api.user.history.dto;

import com.backend.server.model.entity.RentalRestriction;
import com.backend.server.model.entity.enums.RestrictionReason;
import com.backend.server.model.entity.enums.RestrictionType;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class RentalRestrictionResponse {
    private final RestrictionType type;
    private final RestrictionReason reason;
    private final LocalDateTime endAt;
    private final LocalDateTime startAt;

    public RentalRestrictionResponse(RentalRestriction rentalRestriction) {
        type = rentalRestriction.getType();
        reason = rentalRestriction.getReason();
        endAt = rentalRestriction.getEndAt();
        startAt = rentalRestriction.getCreatedAt();
    }
}
