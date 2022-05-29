package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.ERole;
import com.csproject.hrm.entities.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleType, Long> {
    Optional<RoleType> findByERole(ERole eRole);
}
