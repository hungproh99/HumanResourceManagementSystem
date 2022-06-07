package com.csproject.hrm.repositories;

import com.csproject.hrm.common.enums.ERole;
import com.csproject.hrm.entities.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleType, Long> {
  Optional<RoleType> findByERole(ERole eRole);
}
