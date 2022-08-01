package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.ListTimekeepingStatus;
import com.csproject.hrm.repositories.custom.ListTimekeepingStatusRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListTimekeepingStatusRepository
    extends JpaRepository<ListTimekeepingStatus, Long>, ListTimekeepingStatusRepositoryCustom {}
