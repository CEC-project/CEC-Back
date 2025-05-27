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

    public AdminRentalRestrictionListResponse(Page<User> page, List<RentalRestriction> restrictions) {
        pageable = new PageableInfo(page);
        content = IntStream.range(0, restrictions.size())
                .mapToObj(i -> new AdminRentalRestrictionResponse(page.getContent().get(i), restrictions.get(i)))
                .collect(Collectors.toList());
    }
}
