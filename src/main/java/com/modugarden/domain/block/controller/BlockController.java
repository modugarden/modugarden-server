package com.modugarden.domain.block.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.block.dto.response.BlockUserListResponseDto;
import com.modugarden.domain.block.dto.response.BlockUserResponseDto;
import com.modugarden.domain.block.dto.response.UnBlockUserResponseDto;
import com.modugarden.domain.block.service.BlockService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/blocked-list")
public class BlockController {

    private final BlockService blockService;

    @ApiOperation(value = "남의 프로필 - 메인 - 차단", notes = "남의 프로필 - 메인에서 회원을 차단한다.")
    @PostMapping("/{userId}")
    public BaseResponseDto<BlockUserResponseDto> blockUser(@PathVariable Long userId, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(blockService.blockUser(user.getUser(), userId));
    }

    @ApiOperation(value = "내 프로필 - 차단한 사용자", notes = "내 프로필 - 차단한 사용자에서 차단을 해제한다.")
    @DeleteMapping("/{userId}")
    public BaseResponseDto<UnBlockUserResponseDto> unBlockUser(@PathVariable Long userId, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(blockService.unBlockUser(user.getUser(), userId));
    }

    @ApiOperation(value = "내 프로필 - 차단한 사용자", notes = "내 프로필 - 차단한 사용자에서 회원 차단 목록을 조회한다.")
    @GetMapping("")
    public SliceResponseDto<BlockUserListResponseDto> readBlockUser(@AuthenticationPrincipal ModugardenUser user, Pageable pageable) {
        return new SliceResponseDto<>(blockService.readBlockUser(user.getUser(), pageable));
    }
}
