package com.modugarden.domain.block.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.block.dto.response.BlockUserListResponseDto;
import com.modugarden.domain.block.dto.response.BlockUserResponseDto;
import com.modugarden.domain.block.dto.response.UnBlockUserResponseDto;
import com.modugarden.domain.block.entity.UserBlock;
import com.modugarden.domain.block.repository.BlockRepository;
import com.modugarden.domain.follow.repository.FollowRepository;
import com.modugarden.domain.storage.entity.repository.BoardStorageRepository;
import com.modugarden.domain.storage.entity.repository.CurationStorageRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;
    private final BoardStorageRepository boardStorageRepository;
    private final FollowRepository followRepository;
    private final CurationStorageRepository curationStorageRepository;

    @Transactional
    public BlockUserResponseDto blockUser(User user, Long blockUserId) {
        User blockUser = userRepository.findById(blockUserId).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        UserBlock userBlock = new UserBlock(user, blockUser);
        blockRepository.save(userBlock);

        boardStorageRepository.deleteAllByUser_Id(user.getId(), blockUserId);
        curationStorageRepository.deleteAllByUser_Id(user.getId(), blockUserId);
        followRepository.deleteByUserAndFollowingUser(user, blockUser);
        followRepository.deleteByUserAndFollowingUser(blockUser, user);
        return new BlockUserResponseDto(userBlock.getUser().getId(), userBlock.getBlockUser().getId());
    }

    @Transactional
    public UnBlockUserResponseDto unBlockUser(User user, Long unBlockUserId) {
        User unBlockUser = userRepository.findById(unBlockUserId).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));
        UserBlock userBlock = blockRepository.findByUserAndBlockUser(user, unBlockUser).orElseThrow(() -> new BusinessException(ErrorMessage.BLOCKUSER_NOT_FOUND));
        blockRepository.delete(userBlock);
        return new UnBlockUserResponseDto(userBlock.getUser().getId(), userBlock.getBlockUser().getId());
    }

    public Slice<BlockUserListResponseDto> readBlockUser(User user, Pageable pageable) {
        Slice<UserBlock> findUserBlocks = blockRepository.findByUser_Id(user.getId(), pageable);
        Slice<BlockUserListResponseDto> result = findUserBlocks
                .map(b -> new BlockUserListResponseDto(b.getBlockUser().getId(), b.getBlockUser().getNickname()
                        ,b.getBlockUser().getProfileImg(), userRepository.readUserInterestCategory(b.getId())));
        return result;
    }
}
