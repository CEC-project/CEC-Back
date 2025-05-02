package com.backend.server.api.admin.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.model.entity.EquipmentCategory;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/equipment-categories")
@RequiredArgsConstructor
public class AdminEquipmentCategoryController {

    private final AdminEquipmentCategoryService adminEquipmentCategoryService;
    private final EquipmentCategoryService equipmentCategoryService;

    @PostMapping
    public EquipmentCategory createCategory(@RequestBody EquipmentCategory category) {
        return adminEquipmentCategoryService.createCategory(category);
    }

    @PutMapping("/{id}")
    public EquipmentCategory updateCategory(@PathVariable Long id, @RequestBody EquipmentCategory category) {
        return adminEquipmentCategoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        adminEquipmentCategoryService.deleteCategory(id);
    }

    @GetMapping
    public List<EquipmentCategory> getAllCategories() {
        return equipmentCategoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public EquipmentCategory getCategoryById(@PathVariable Long id) {
        return equipmentCategoryService.getCategoryById(id);
    }
}

