package com.userhub.service;

import com.userhub.dto.UserResponse;
import com.userhub.entity.User;
import com.userhub.exception.ApiException;
import com.userhub.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @Transactional
    public UserResponse updateFullName(Long userId, String fullName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        user.setFullName(fullName.trim());
        return UserResponse.from(userRepository.save(user));
    }
}
