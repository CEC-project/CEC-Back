
package com.backend.server.model.repository;

import com.backend.server.model.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    }
