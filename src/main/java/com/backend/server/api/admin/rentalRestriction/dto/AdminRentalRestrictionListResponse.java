package com.backend.server.api.admin.rentalRestriction.dto;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.RentalRestriction;
import com.backend.server.model.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class AdminRentalRestrictionListResponse {
    private List<AdminRentalRestrictionResponse> content;
    private PageableInfo pageable;

    public AdminRentalRestrictionListResponse(
            Page<RentalRestriction> page,
            List<User> users) {
        pageable = new PageableInfo(page);
        content = IntStream.range(0, page.getNumberOfElements())
                .mapToObj(i -> new AdminRentalRestrictionResponse(users.get(i), page.getContent().get(i)))
                .collect(Collectors.toList());
    }
}
