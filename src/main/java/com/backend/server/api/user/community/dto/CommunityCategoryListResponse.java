package com.backend.server.api.user.community.dto;

import com.backend.server.model.entity.BoardCategory;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Data
public class CommunityCategoryListResponse {
    private Long id;

    private String name;

    private String description;
}
