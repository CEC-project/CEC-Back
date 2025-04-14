package com.backend.server.api.admin.controller;

import com.backend.server.api.admin.service.AdminEquipmentService;
import com.backend.server.api.admin.dto.AdminEquipmentCreateRequest;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
@Tag(name = "Equipment API", description = "장비 관리 API")
public class AdminEquipmentController {

    private final AdminEquipmentService adminEquipmentService;

    @GetMapping("/admin-users")
    @Operation(summary = "관리자 목록 조회", description = "등록 가능한 관리자 목록을 조회합니다")
    public ResponseEntity<List<User>> getAdminUsers() {
        List<User> adminUsers = adminEquipmentService.getAdminUsers();
        return ResponseEntity.ok(adminUsers);
    }
    
    @GetMapping("/grades")
    @Operation(summary = "학년 목록 조회", description = "대여 제한 가능한 학년 목록을 조회합니다")
    public ResponseEntity<List<Integer>> getGrades() {
        // 1, 2, 3, 4학년 반환
        List<Integer> grades = IntStream.rangeClosed(1, 4)
                .boxed()
                .collect(Collectors.toList());
        return ResponseEntity.ok(grades);
    }

    @PostMapping
    @Operation(summary = "장비 등록", description = "새로운 장비를 등록합니다")
    public ResponseEntity<Equipment> registerEquipment(@Valid @RequestBody AdminEquipmentCreateRequest requset) {
        Equipment savedEquipment = adminEquipmentService.createEquipment(requset);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEquipment);
    }

    @GetMapping
    @Operation(summary = "장비 목록 조회", description = "모든 장비 목록을 조회합니다")
    public ResponseEntity<List<Equipment>> getAllEquipments() {
        List<Equipment> equipments = adminEquipmentService.getAllEquipments();
        return ResponseEntity.ok(equipments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "개별 장비 조회", description = "ID로 특정 장비를 조회합니다")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        return adminEquipmentService.getEquipmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "카테고리별 장비 조회", description = "특정 카테고리의 장비 목록을 조회합니다")
    public ResponseEntity<List<Equipment>> getEquipmentsByCategory(@PathVariable String category) {
        List<Equipment> equipments = adminEquipmentService.getEquipmentsByCategory(category);
        return ResponseEntity.ok(equipments);
    }

    @PutMapping("/{id}")
    @Operation(summary = "장비 정보 수정", description = "등록된 장비 정보를 수정합니다")
    public ResponseEntity<Equipment> updateEquipment(
            @PathVariable Long id,
            @Valid @RequestBody AdminEquipmentCreateRequest dto) {
        try {
            Equipment updatedEquipment = adminEquipmentService.updateEquipment(id, dto);
            return ResponseEntity.ok(updatedEquipment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "장비 삭제", description = "등록된 장비를 삭제합니다")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        try {
            adminEquipmentService.deleteEquipment(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 