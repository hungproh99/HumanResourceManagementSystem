package com.csproject.hrm.repositories.custom;

import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface CheckInCheckOutRepositoryCustom {
  void insertCheckInCheckOutByTimekeepingId(Long timekeepingId, LocalTime localTime);
}
