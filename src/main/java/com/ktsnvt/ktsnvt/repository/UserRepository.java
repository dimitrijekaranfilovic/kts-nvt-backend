package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
