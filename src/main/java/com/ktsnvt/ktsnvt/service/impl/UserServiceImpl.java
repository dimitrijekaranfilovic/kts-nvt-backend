package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.UserNotFoundException;
import com.ktsnvt.ktsnvt.model.User;
import com.ktsnvt.ktsnvt.repository.UserRepository;
import com.ktsnvt.ktsnvt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User readForSalaryUpdate(Integer id) {
        return userRepository
                .readForSalaryUpdate(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id: %d does not exist.", id)));
    }
}
