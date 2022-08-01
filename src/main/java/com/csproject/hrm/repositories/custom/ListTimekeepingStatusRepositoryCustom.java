package com.csproject.hrm.repositories.custom;

import org.springframework.stereotype.Repository;

@Repository
public interface ListTimekeepingStatusRepositoryCustom {
  void insertListTimekeepingStatus(Long timekeepingId, String timekeepingStatus);
}
