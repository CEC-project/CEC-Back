package com.backend.server.api.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.server.api.common.dto.CommonCategoryResponse;
import com.backend.server.api.common.service.CommonCategoryReadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class UserCategoryController {
    private final CommonCategoryReadService categoryReadService;

    @GetMapping
    public ResponseEntity<List<CommonCategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryReadService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonCategoryResponse> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryReadService.getCategory(id));
    }
} 