package com.modugarden.domain.block.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.common.response.SliceResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.block.dto.response.BlockUserListResponseDto;
import com.modugarden.domain.block.dto.response.BlockUserResponseDto;
import com.modugarden.domain.block.dto.response.UnBlockUserResponseDto;
import com.modugarden.domain.block.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/blocked-list")
public class BlockController {

    private final BlockService blockService;

    @PostMapping("/{userId}")
    public BaseResponseDto<BlockUserResponseDto> blockUser(@PathVariable Long userId, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(blockService.blockUser(user.getUser(), userId));
    }

    @DeleteMapping("/{userId}")
    public BaseResponseDto<UnBlockUserResponseDto> unBlockUser(@PathVariable Long userId, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(blockService.unBlockUser(user.getUser(), userId));
    }

    @GetMapping("")
    public SliceResponseDto<BlockUserListResponseDto> readBlockUser(@AuthenticationPrincipal ModugardenUser user, Pageable pageable) {
        return new SliceResponseDto<>(blockService.readBlockUser(user.getUser(), pageable));
    }
}
