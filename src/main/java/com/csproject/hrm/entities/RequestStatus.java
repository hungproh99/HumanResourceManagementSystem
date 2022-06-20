package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.ERequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "request_status")
public class RequestStatus {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "type_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "name")
  private ERequestStatus eRequestStatus;

  @OneToOne(mappedBy = "requestStatus", fetch = FetchType.LAZY)
  private ApplicationsRequest applicationsRequest;
}
