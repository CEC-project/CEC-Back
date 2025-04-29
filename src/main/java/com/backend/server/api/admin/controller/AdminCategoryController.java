package com.backend.server.api.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.server.api.admin.dto.category.AdminCategoryRequest;
import com.backend.server.api.admin.service.AdminCategoryService;
import com.backend.server.api.common.dto.CommonCategoryResponse;
import com.backend.server.api.common.service.CommonCategoryReadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Tag(name = "Category Admin API", description = "카테고리 관리 어드민 API")
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;
    private final CommonCategoryReadService categoryReadService;

    @GetMapping
    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다")
    public ResponseEntity<List<CommonCategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryReadService.getAllCategories());
    }


    //단일조회는 지금으로써는 필요없지만 혹시몰라 만들었어요
    @GetMapping("/{id}")
    @Operation(summary = "단일 카테고리 조회", description = "ID로 특정 카테고리를 조회합니다")
    public ResponseEntity<CommonCategoryResponse> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryReadService.getCategory(id));
    }

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다")
    public ResponseEntity<CommonCategoryResponse> createCategory(@RequestBody AdminCategoryRequest request) {
        return ResponseEntity.ok(adminCategoryService.createCategory(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "카테고리 수정", description = "ID로 특정 카테고리를 수정합니다")
    public ResponseEntity<CommonCategoryResponse> updateCategory(
            @PathVariable Long id,
            @RequestBody AdminCategoryRequest request) {
        return ResponseEntity.ok(adminCategoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "카테고리 삭제", description = "ID로 특정 카테고리를 삭제합니다")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        adminCategoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
} 