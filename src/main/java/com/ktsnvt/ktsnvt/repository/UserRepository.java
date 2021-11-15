package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :id and u.isActive = true")
    Optional<User> readForSalaryUpdate(Integer id);
}
