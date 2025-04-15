package com.backend.server.api.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.server.api.common.dto.CommonCategoryResponse;
import com.backend.server.api.common.service.CommonCategoryReadService;
import com.backend.server.api.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class UserCategoryController {
    private final CommonCategoryReadService categoryReadService;

    //API 설계서를 보면 대부분의 응답에서는 
    //{
    //    "status": "success",
    //    "message": "카테고리 목록 조회 성공",
    //    "data": [
    //        {
    //            "id": 1,
    //            "name": "카테고리 이름"
    //        }
    //이런식으로 stauts랑 message가 있어서 이걸 ApiResponse라는 클래스를 만들어서
    @GetMapping
    public ApiResponse<List<CommonCategoryResponse>> getAllCategories() {
        //이렇게 공통적으로 사용할 수 있도록 만들었어요
        return ApiResponse.success("카테고리 목록 조회 성공", categoryReadService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ApiResponse<CommonCategoryResponse> getCategory(@PathVariable Long id) {
        return ApiResponse.success("카테고리 조회 성공", categoryReadService.getCategory(id));
    }
} 