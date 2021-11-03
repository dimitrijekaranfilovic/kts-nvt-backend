package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.SuperUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperUserRepository extends JpaRepository<SuperUser, Integer> {
}
