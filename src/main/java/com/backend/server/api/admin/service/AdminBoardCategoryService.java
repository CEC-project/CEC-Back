package com.backend.server.api.admin.service;

import com.backend.server.api.admin.dto.category.AdminCommonCategoryRequest;
import com.backend.server.api.admin.dto.category.AdminCommonCategoryResponse;
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
    public List<AdminCommonCategoryResponse> getBoardCategoryList() {
        return boardCategoryRepository.getBoardCategoryList();
    }

    public Long createBoardCategory(AdminCommonCategoryRequest request) {
        BoardCategory boardCategory = BoardCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return boardCategoryRepository.save(boardCategory).getId();
    }

    @Transactional
    public Long updateBoardCategory(Long id, AdminCommonCategoryRequest request) {
        BoardCategory BoardCategory = boardCategoryRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        BoardCategory.toBuilder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        boardCategoryRepository.save(BoardCategory);
        return BoardCategory.getId();
    }

    public void deleteBoardCategory(Long id) {
        boardCategoryRepository.deleteById(id);
    }
}
