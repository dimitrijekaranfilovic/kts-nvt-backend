package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.UserNotFoundException;
import com.ktsnvt.ktsnvt.model.User;
import com.ktsnvt.ktsnvt.repository.UserRepository;
import com.ktsnvt.ktsnvt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends TransactionalServiceBase implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User readForSalaryUpdate(Integer id) {
        return userRepository
                .readForSalaryUpdate(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id: %d does not exist.", id)));
    }
}
