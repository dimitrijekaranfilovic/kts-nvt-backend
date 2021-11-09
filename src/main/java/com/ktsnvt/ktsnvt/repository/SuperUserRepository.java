package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.SuperUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuperUserRepository extends JpaRepository<SuperUser, Integer> {

    Optional<SuperUser> findByEmail(String email);

}
