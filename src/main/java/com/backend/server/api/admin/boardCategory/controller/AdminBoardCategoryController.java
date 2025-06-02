package com.backend.server.api.admin.boardCategory.controller;

import com.backend.server.api.admin.boardCategory.dto.AdminBoardCategoryRequest;
import com.backend.server.api.admin.boardCategory.dto.AdminBoardCategoryResponse;
import com.backend.server.api.admin.boardCategory.service.AdminBoardCategoryService;
import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/board-category")
@RequiredArgsConstructor
@Tag(name = "1-2. 카테고리 관리 / 게시글 유형 관리", description = "작업 완료")
public class AdminBoardCategoryController {

    private final AdminBoardCategoryService adminBoardCategoryService;

    @GetMapping
    public ApiResponse<List<AdminBoardCategoryResponse>> getBoardCategories() {
        return ApiResponse.success("게시판 카테고리 목록 조회 성공", adminBoardCategoryService.getBoardCategoryList());
    }

    @PostMapping
    public ApiResponse<Long> postBoardCategory(@RequestBody AdminBoardCategoryRequest request) {
        return ApiResponse.success("게시판 카테고리 등록 성공", adminBoardCategoryService.createBoardCategory(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Long> putBoardCategory(
            @PathVariable Long id,
            @RequestBody AdminBoardCategoryRequest request) {
        return ApiResponse.success("게시판 카테고리 수정 성공", adminBoardCategoryService.updateBoardCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBoardCategory(
            @PathVariable Long id) {
        adminBoardCategoryService.deleteBoardCategory(id);
        return ApiResponse.success("게시판 카테고리 삭제 성공", null);
    }
}