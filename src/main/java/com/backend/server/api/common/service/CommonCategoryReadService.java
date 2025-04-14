package com.backend.server.api.common.service;

import java.util.List;
import com.backend.server.api.common.dto.CommonCategoryResponse;

public interface CommonCategoryReadService {
    List<CommonCategoryResponse> getAllCategories();

    //이거 혹시몰라서 만든거에요
    CommonCategoryResponse getCategory(Long id);
} 