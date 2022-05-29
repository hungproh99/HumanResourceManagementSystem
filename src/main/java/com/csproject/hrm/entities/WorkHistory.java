package com.csproject.hrm.entities;

import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkHistory {
	private Long historyID;
	private Timestamp history;
}