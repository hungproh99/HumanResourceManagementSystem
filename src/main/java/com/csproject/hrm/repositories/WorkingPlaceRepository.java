package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.WorkingPlace;
import com.csproject.hrm.repositories.custom.WorkingPlaceRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingPlaceRepository
    extends JpaRepository<WorkingPlace, Long>, WorkingPlaceRepositoryCustom {}
