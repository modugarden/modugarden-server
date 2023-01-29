package com.modugarden.domain.follow.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.follow.dto.FollowRecommendResponseDto;
import com.modugarden.domain.follow.dto.FollowersResponseDto;
import com.modugarden.domain.follow.dto.FollowingsResponseDto;
import com.modugarden.domain.follow.dto.isFollowedResponseDto;
import com.modugarden.domain.follow.entity.Follow;
import com.modugarden.domain.follow.repository.FollowRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.modugarden.common.error.enums.ErrorMessage.USER_NOT_FOUND;
//여기서는 인자에 @ 사용 안함
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository; //@autoWired 대신에 private final로 사용
    //팔로우
    @Transactional
    public isFollowedResponseDto follow(ModugardenUser user, Long id) {
        User oToUser = userRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.FOLLOW_NOT_FOUND)); //예외처리
        Follow follow = new Follow(user.getUser(), oToUser);
        followRepository.save(follow);
        return new isFollowedResponseDto(true);
    }
    //팔로우 취소
    @Transactional
    //변화가 필요할 때 transactional 사용
    public isFollowedResponseDto unFollow(ModugardenUser user, Long id) {
        User oToUser = userRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.FOLLOW_NOT_FOUND));  //예외처리
        followRepository.deleteByUser_IdAndFollowingUser_Id(user.getUserId(), oToUser.getId());
        return new isFollowedResponseDto(true);
    }


    //팔로우 유무 체크
    public isFollowedResponseDto profile(Long id, ModugardenUser user) {
        User oToUser = userRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.FOLLOW_NOT_FOUND));  //예외처리
        //User 대신 user 객체가 와야 함
        boolean followcheck = followRepository.exists(user.getUserId(),id);
        return new isFollowedResponseDto(followcheck);
    }

    //user가 following user을 following 함
    //following user을 user가 follower 함

    //내 팔로워 명단조회
    public Slice<FollowersResponseDto> meFollowerList(Long id, Pageable pageable) {
        Slice<User> followers = followRepository.findByFollowingUser_Id(id, pageable);
        Slice<FollowersResponseDto> result = followers
                .map(u -> new FollowersResponseDto(u.getId(), u.getNickname(), u.getProfileImg()
                        , userRepository.readUserInterestCategory((u.getId()))
                        , followRepository.exists(id, u.getId())));
        return result;
    }
    //내 팔로잉 명단조회
    public Slice<FollowingsResponseDto> meFollowingList(Long id, Pageable pageable) {
        Slice<User> followings = followRepository.findByUser_Id(id, pageable);
        Slice<FollowingsResponseDto> result = followings
                .map(u -> new FollowingsResponseDto(u.getId(), u.getNickname(), u.getProfileImg()
                        , userRepository.readUserInterestCategory((u.getId()))
                        , followRepository.exists(id, u.getId())));
        return result;
    }
    //타인 팔로워 명단조회
    public Slice<FollowersResponseDto> othersFollowerList(Long id, Long otherId, Pageable pageable) {
        Slice<User> followers = followRepository.findByFollowingUser_Id(otherId, pageable);
        Slice<FollowersResponseDto> result = followers
                .map(u -> new FollowersResponseDto(u.getId(), u.getNickname(), u.getProfileImg()
                        , userRepository.readUserInterestCategory((u.getId()))
                        , followRepository.exists(otherId, u.getId())));
        return result;
    }
    //타인 팔로잉 명단조회
    public Slice<FollowingsResponseDto> othersFollowingList(Long id, Long otherId, Pageable pageable) {
        Slice<User> followings = followRepository.findByUser_Id(otherId, pageable);
        Slice<FollowingsResponseDto> result = followings
                .map(u -> new FollowingsResponseDto(u.getId(), u.getNickname(), u.getProfileImg()
                        , userRepository.readUserInterestCategory((u.getId()))
                        , followRepository.exists(otherId, u.getId())));
        return result;
    }
    // 팔로우할 유저 추천
    public List<FollowRecommendResponseDto> recommendFollowingList(User user, Pageable pageable){
        List<FollowRecommendResponseDto> responseDto = new ArrayList<>();

        List<Long> recommendUserIds = followRepository.recommend3FollowingId(user, pageable.getOffset(), pageable.getPageSize());
        for (Long recommendUserId : recommendUserIds) {
            User recommendUser = userRepository.findById(recommendUserId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
            List<String> interestCategoryList = userRepository.readUserInterestCategory(recommendUserId);

            responseDto.add(new FollowRecommendResponseDto(recommendUser.getNickname(), recommendUser.getProfileImg(), interestCategoryList));
        }
        return responseDto;
    }
}
