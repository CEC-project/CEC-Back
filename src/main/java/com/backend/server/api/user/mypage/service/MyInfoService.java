package com.backend.server.api.user.mypage.service;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.mypage.dto.MyInfoRequest;
import com.backend.server.api.user.mypage.dto.MyInfoResponse;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyInfoService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MyInfoResponse getMyInfo(LoginUser loginUser) {
        return new MyInfoResponse(loginUser);
    }

    public MyInfoResponse updateMyInfo(MyInfoRequest request, LoginUser loginUser) {
        User user = userRepository.findById(loginUser.getId()).orElseThrow(()->new IllegalArgumentException("이사람  지금 로그인 아닌듯요"));

        user = user.toBuilder()
                .name(request.getName())
                .nickname(request.getNickname())
                .phoneNumber(request.getPhoneNumber())
                .build();

        if (request.getNewPassword() != null && request.getNewPassword().equals(request.getConfirmPassword())) {
            user = user.toBuilder()
                    .password(passwordEncoder.encode(request.getNewPassword()))
                    .build();
        }

        if (request.getProfilePicture() != null && !request.getProfilePicture().isBlank()) {
            user = user.toBuilder()
                    .profilePicture(request.getProfilePicture())
                    .build();
        }

        userRepository.save(user);
        return new MyInfoResponse(loginUser);

    }

}