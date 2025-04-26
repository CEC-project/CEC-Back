package com.backend.server.api.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.backend.server.api.admin.service.AdminImportExcelService;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Excel API", description = "엑셀 파일 관련 API")
public class AdminImportExcelController {

    @Autowired
    private AdminImportExcelService excelService;

    @PostMapping(value = "/import-users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "엑셀 파일로 사용자 정보 일괄 등록", 
        description = "엑셀 파일을 업로드하여 사용자 정보를 일괄 등록합니다."
    )
    public ResponseEntity<String> importUsers(@RequestParam("file") MultipartFile file) {
        try {
            excelService.importUsersFromExcel(file);
            return ResponseEntity.ok("사용자 정보가 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("파일 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
} 