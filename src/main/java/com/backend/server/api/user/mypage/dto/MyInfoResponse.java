package com.backend.server.api.user.mypage.dto;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.model.entity.User;
import lombok.Getter;

@Getter
public class MyInfoResponse {
    private String name;
    private String phoneNumber;
    private String nickname;
    private String profilePicture;


    public MyInfoResponse(LoginUser user) {
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        this.nickname = user.getNickname();
        this.profilePicture = user.getProfilePicture();
    }
}