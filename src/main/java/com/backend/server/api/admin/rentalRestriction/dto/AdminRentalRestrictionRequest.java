package com.backend.server.api.admin.rentalRestriction.dto;

import com.backend.server.model.entity.RentalRestriction;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.RestrictionReason;
import com.backend.server.model.entity.enums.RestrictionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRentalRestrictionRequest {
    @NotNull
    RestrictionType type;

    @NotNull
    RestrictionReason reason;

    @Positive
    Integer duration;

    public RentalRestriction toEntity(User user) {
        return RentalRestriction.builder()
                .endAt(LocalDateTime.now().plusDays(duration))
                .type(type)
                .reason(reason)
                .user(user)
                .build();
    }
}