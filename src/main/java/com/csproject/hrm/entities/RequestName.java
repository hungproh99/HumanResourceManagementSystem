package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.ERequestName;
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
@Table(name = "request_name")
public class RequestName {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "type_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "name")
  private ERequestName eRequestName;

  @OneToOne(mappedBy = "requestName", fetch = FetchType.LAZY)
  private ApplicationsRequest applicationsRequest;

  @ManyToOne(cascade = CascadeType.ALL, targetEntity = RequestType.class)
  @JoinColumn(name = "request_type_id")
  private RequestType requestType;
}
