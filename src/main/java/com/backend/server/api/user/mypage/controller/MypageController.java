package com.backend.server.api.user.mypage.controller;

import com.backend.server.api.common.dto.ApiResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.mypage.dto.MyInfoRequest;
import com.backend.server.api.user.mypage.dto.MyInfoResponse;
import com.backend.server.api.user.mypage.service.MyInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Tag(name = "3-1. 유저 프로필 / 내 정보 수정", description = "수정 완")
public class MypageController {
    private final MyInfoService myinfoService;
    //내정보조회
    @Operation(
            summary = "내 정보 조회"
    )
    @GetMapping
    public ApiResponse<MyInfoResponse> getMyInfo(@AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("내 정보 조회 완료", myinfoService.getMyInfo(loginUser));
    }

    @Operation(
            summary = "내 정보 수정"
    )
    @PutMapping
    public ApiResponse<MyInfoResponse> updateMyInfo(@RequestBody MyInfoRequest request, @AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success("내 정보 수정 완료", myinfoService.updateMyInfo(request, loginUser));
    }
}
