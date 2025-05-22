package com.backend.server.api.admin.rentalRestriction.service;

import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestriction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminRentalRestrictionService {



    @Transactional(readOnly = true)
    public AdminRestrictionListResponse getRentalRestrictedUsers(AdminRestrictionListRequest request) {
        return null;
    }

    @Transactional(readOnly = true)
    public AdminRentalAllowedListResponse getRentalAllowedUsers(AdminRentalAllowedListRequest request) {
        return null;
    }

    @Transactional
    public Long addRentalRestriction(Long userId, AdminRentalRestriction request) {
        return null;
    }

    @Transactional
    public void cancelRentalRestriction(Long id) {
    }
}