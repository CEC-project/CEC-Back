package com.backend.server.api.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.server.api.admin.dto.category.AdminClassRoomCreateRequest;
import com.backend.server.api.admin.service.AdminCategoryService;
import com.backend.server.api.common.dto.CommonCategoryResponse;
import com.backend.server.api.common.service.CommonCategoryReadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;
    private final CommonCategoryReadService categoryReadService;

    @GetMapping
    public ResponseEntity<List<CommonCategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryReadService.getAllCategories());
    }


    //단일조회는 지금으로써는 필요없지만 혹시몰라 만들었어요
    @GetMapping("/{id}")
    public ResponseEntity<CommonCategoryResponse> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryReadService.getCategory(id));
    }

    @PostMapping
    public ResponseEntity<CommonCategoryResponse> createCategory(@RequestBody AdminClassRoomCreateRequest request) {
        return ResponseEntity.ok(adminCategoryService.createCategory(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonCategoryResponse> updateCategory(
            @PathVariable Long id,
            @RequestBody AdminClassRoomCreateRequest request) {
        return ResponseEntity.ok(adminCategoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        adminCategoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
} 