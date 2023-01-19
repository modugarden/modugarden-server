package com.modugarden.domain.user.service;

import com.modugarden.domain.user.dto.UserNicknameFindResponseDto;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
        }
        return null;
    }

    public Slice<UserNicknameFindResponseDto> findByNickname(String nickname, Pageable pageable) {
        Slice<User> findUsers = userRepository.findByNicknameLike('%' + nickname + '%', pageable);
        Slice<UserNicknameFindResponseDto> result = findUsers.map(u -> new UserNicknameFindResponseDto(u.getId()));
        return result;
    }
}
