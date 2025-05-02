// package com.backend.server.api.user.controller;

// import java.util.List;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.backend.server.model.entity.EquipmentModel;

// import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/api/equipment-models")
// @RequiredArgsConstructor
// public class EquipmentModelController {

//     private final EquipmentModelService equipmentModelService;

//     @GetMapping
//     public List<EquipmentModel> getAllModels() {
//         return equipmentModelService.getAllModels();
//     }

//     @GetMapping
//     public List<EquipmentModel> getAllModels() {
//         return equipmentModelService.getAllModels();
//     }

//     @GetMapping("/{id}")
//     public EquipmentModel getModelById(@PathVariable Long id) {
//         return equipmentModelService.getModelById(id);
//     }

//     @PostMapping
//     public EquipmentModel createModel(@RequestBody EquipmentModel model) {
//         return equipmentModelService.createModel(model);
//     }

//     @PutMapping("/{id}")
//     public EquipmentModel updateModel(@PathVariable Long id, @RequestBody EquipmentModel model) {
//         return equipmentModelService.updateModel(id, model);
//     }

//     @DeleteMapping("/{id}")
//     public void deleteModel(@PathVariable Long id) {
//         equipmentModelService.deleteModel(id);
//     }
// }