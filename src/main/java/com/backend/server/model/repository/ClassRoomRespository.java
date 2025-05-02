package com.backend.server.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.backend.server.model.entity.ClassRoom;

public interface ClassRoomRespository extends JpaRepository<ClassRoom, Long>, JpaSpecificationExecutor<ClassRoom> {
    
}
