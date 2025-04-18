// package com.backend.server.api.common.controller;

// import com.backend.server.api.admin.service.AdminCategoryService;
// import com.backend.server.api.common.dto.CommonCategoryResponse;

// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import java.util.List;
// import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/api/categories")
// @RequiredArgsConstructor
// @Tag(name = "Category API", description = "카테고리 조회 API")
// public class CategoryController {

//     private final AdminCategoryService categoryService;


//     //현재 DTO에서     @NotBlank(message = "카테고리 이름은 필수입니다")
//     //이거 쓰는데 문제없겠죠?

//     @GetMapping
//     @Operation(summary = "카테고리 목록 조회", description = "전체 카테고리 목록을 조회합니다.")
//     public ResponseEntity<List<CommonCategoryResponse>> getAllCategories() {
//         List<CommonCategoryResponse> categories = categoryService.getAllCategories().stream()
//                 .map(Dto -> CommonCategoryResponse.builder()
//                         .id(Dto.getId())
//                         .name(Dto.getName())
//                         .build())
//                 .collect(Collectors.toList());
                
//         return ResponseEntity.ok(categories);
//     }
// } 