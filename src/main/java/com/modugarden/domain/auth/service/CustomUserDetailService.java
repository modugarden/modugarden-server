package com.modugarden.domain.auth.service;

import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.modugarden.common.error.enums.ErrorMessage.USER_NOT_FOUND;


@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
        System.out.println(email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        return new ModugardenUser(user);
    }
}
