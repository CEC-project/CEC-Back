package com.backend.server.api.admin.boardCategory.service;

import com.backend.server.api.admin.boardCategory.dto.AdminBoardCategoryRequest;
import com.backend.server.api.admin.boardCategory.dto.AdminBoardCategoryResponse;
import com.backend.server.model.entity.BoardCategory;
import com.backend.server.model.repository.BoardCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminBoardCategoryService {
    private final BoardCategoryRepository boardCategoryRepository;
    public List<AdminBoardCategoryResponse> getBoardCategoryList() {
        return boardCategoryRepository.getBoardCategoryList();
    }

    public Long createBoardCategory(AdminBoardCategoryRequest request) {
        BoardCategory boardCategory = BoardCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return boardCategoryRepository.save(boardCategory).getId();
    }

    @Transactional
    public Long updateBoardCategory(Long id, AdminBoardCategoryRequest request) {
        BoardCategory boardCategory = boardCategoryRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        boardCategory = boardCategory.toBuilder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        boardCategory = boardCategoryRepository.save(boardCategory);
        return boardCategory.getId();
    }

    public void deleteBoardCategory(Long id) {
        boardCategoryRepository.deleteById(id);
    }
}
