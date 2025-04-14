package com.backend.server.api.admin.controller;

import com.backend.server.api.admin.service.AdminCategoryService;
import com.backend.server.api.common.dto.CategoryResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Tag(name = "Admin Category API", description = "관리자용 카테고리 관리 API")
public class AdminCategoryController {

    private final AdminCategoryService categoryService;

    @GetMapping
    @Operation(summary = "카테고리 목록 조회", description = "전체 카테고리 목록을 조회합니다.")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    @Operation(summary = "카테고리 등록", description = "새로운 카테고리를 등록합니다.")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryResponse categoryDto) {
        try {
            CategoryResponse createdCategory = categoryService.createCategory(categoryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "카테고리 수정", description = "기존 카테고리 정보를 수정합니다.")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryResponse request) {
        try {
            CategoryResponse updatedCategory = categoryService.updateCategory(id, request);
            return ResponseEntity.ok(updatedCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 