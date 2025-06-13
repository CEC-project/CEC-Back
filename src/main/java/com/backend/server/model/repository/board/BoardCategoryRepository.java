package com.backend.server.model.repository.board;

import com.backend.server.api.admin.boardCategory.dto.AdminBoardCategoryResponse;
import com.backend.server.model.entity.BoardCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {

    @Query("SELECT new com.backend.server.api.admin.boardCategory.dto.AdminBoardCategoryResponse" +
            "(p.id, p.name, p.description, COUNT(u), p.createdAt) " +
            "FROM BoardCategory p LEFT JOIN p.posts u " +
            "GROUP BY p.id")
    List<AdminBoardCategoryResponse> getBoardCategoryList();
}