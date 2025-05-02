package com.backend.server.model.repository;


import com.backend.server.model.entity.InquiryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryTypeRepository extends JpaRepository<InquiryType, Long> {
}
