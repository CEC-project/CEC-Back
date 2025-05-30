
package com.backend.server.model.repository;

import com.backend.server.api.user.inquiry.dto.InquiryResponse;
import com.backend.server.model.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>, JpaSpecificationExecutor<Inquiry> {

    @EntityGraph(attributePaths = {"answers", "user", "user.professor"})
    Page<Inquiry> findAll(Specification<Inquiry> spec, Pageable pageable);

    List<InquiryResponse> findAllByAuthorId(Long authorId); //사용자 ID로 조회
}
