package com.modugarden.domain.block.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.block.dto.response.BlockUserResponseDto;
import com.modugarden.domain.block.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/blocked-list")
public class BlockController {

    private final BlockService blockService;

    @PostMapping("/{userId}")
    public BaseResponseDto<BlockUserResponseDto> blockUser(@PathVariable Long userId, @AuthenticationPrincipal ModugardenUser user) {
        return new BaseResponseDto<>(blockService.blockUser(user.getUser(), userId));
    }
}
