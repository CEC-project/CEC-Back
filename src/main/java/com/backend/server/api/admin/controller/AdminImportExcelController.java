package com.backend.server.api.admin.controller;

import com.backend.server.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.backend.server.api.admin.service.AdminImportExcelService;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Excel Admin API", description = "액셀 파일에서 유저 정보 추출하는 어드민 API")
public class AdminImportExcelController {

    @Autowired
    private AdminImportExcelService excelService;

    @PostMapping(value = "/import-users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "엑셀 파일로 사용자 정보 일괄 등록", 
        description = "엑셀 파일을 업로드하여 사용자 정보를 일괄 등록합니다."
    )
    public ApiResponse<Integer> importUsers(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.success("사용자 정보가 성공적으로 등록되었습니다.", excelService.importUsersFromExcel(file));
    }
} 