package com.modugarden.domain.user.controller;

import com.modugarden.domain.follow.repository.FollowRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowRepository followRepository;
}
