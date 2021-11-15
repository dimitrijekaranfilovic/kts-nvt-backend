package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    @Query("select a from Authority a where a.name = :name and a.isActive = true")
    Optional<Authority> findByName(String name);

}
