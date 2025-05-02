package com.backend.server.api.user.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.api.user.dto.equipment.equipmentCategory.EquipmentCategoryResponse;
import com.backend.server.api.user.service.EquipmentCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/equipment-categories")
@RequiredArgsConstructor
public class EquipmentCategoryController {

    private final EquipmentCategoryService categoryService;

    @GetMapping
    public List<EquipmentCategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public EquipmentCategoryResponse getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
}