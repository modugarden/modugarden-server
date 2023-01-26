package com.modugarden.domain.block.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.block.dto.response.BlockUserResponseDto;
import com.modugarden.domain.block.entity.UserBlock;
import com.modugarden.domain.block.repository.BlockRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    public BlockUserResponseDto blockUser(User user, Long blockUserId) {
        User blockUser = userRepository.findById(blockUserId).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        UserBlock userBlock = new UserBlock(user, blockUser);
        blockRepository.save(userBlock);
        return new BlockUserResponseDto(userBlock.getUser().getId(), userBlock.getBlockUser().getId());
    }
}
