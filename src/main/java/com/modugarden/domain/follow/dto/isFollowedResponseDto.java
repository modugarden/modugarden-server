package com.modugarden.domain.follow.dto;

import com.modugarden.domain.follow.entity.Follow;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class isFollowedResponseDto {
    private boolean isFollow;
}
