package com.backend.server.api.admin.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminListOverviewResponse {
    List<Long> user_id;
    List<String> name;
    List<String> nickname;
    List<String> role;
}
