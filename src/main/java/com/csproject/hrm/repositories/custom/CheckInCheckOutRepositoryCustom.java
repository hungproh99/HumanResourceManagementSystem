package com.csproject.hrm.repositories.custom;

import com.csproject.hrm.dto.dto.CheckInCheckOutDto;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface CheckInCheckOutRepositoryCustom {
  void insertCheckInByTimekeepingId(Long timekeepingId, LocalTime localTime);

  void updateCheckOutByTimekeepingId(Long checkInCheckOutId, LocalTime localTime);

  Optional<CheckInCheckOutDto> getLastCheckInCheckOutByTimekeeping(Long timekeepingId);
}
