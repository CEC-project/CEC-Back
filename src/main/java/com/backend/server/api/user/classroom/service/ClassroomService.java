package com.backend.server.api.user.classroom.service;

import com.backend.server.api.user.classroom.dto.ClassroomRentalRequest;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.classroom.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long rental(Long renterId, ClassroomRentalRequest request) {
        return null;
    }
}
