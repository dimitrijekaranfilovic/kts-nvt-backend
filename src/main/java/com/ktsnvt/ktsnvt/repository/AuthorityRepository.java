package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    Optional<Authority> findByName(String name);

}
