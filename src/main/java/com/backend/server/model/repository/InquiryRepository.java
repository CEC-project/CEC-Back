
package com.backend.server.model.repository;

import com.backend.server.api.user.inquiry.dto.InquiryResponse;
import com.backend.server.model.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

        List<InquiryResponse> findAllbyAuthorId(Long authorId); //사용자 ID로 조회
    }
