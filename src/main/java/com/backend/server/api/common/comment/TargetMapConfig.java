package com.backend.server.api.common.comment;

import com.backend.server.model.entity.enums.TargetType;
import com.backend.server.model.repository.InquiryRepository;
import com.backend.server.model.repository.NoticeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

@Configuration
public class TargetMapConfig {
    @Bean
    public Map<TargetType, JpaRepository<?, Long>> targetRepositoryMap(
            InquiryRepository inquiryRepository,
            NoticeRepository noticeRepository
    ) {
        return Map.of(
                TargetType.INQUIRY, inquiryRepository,
                TargetType.NOTICE, noticeRepository
        );
    }
}
