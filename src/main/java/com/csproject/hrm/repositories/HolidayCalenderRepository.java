package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.HolidayCalender;
import com.csproject.hrm.repositories.custom.HolidayCalenderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayCalenderRepository
    extends JpaRepository<HolidayCalender, Long>, HolidayCalenderRepositoryCustom {}
