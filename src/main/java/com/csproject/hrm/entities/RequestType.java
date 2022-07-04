package com.csproject.hrm.entities;

import com.csproject.hrm.common.enums.ERequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "request_type")
public class RequestType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "type_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "name")
  private ERequestType eRequestType;

  @OneToOne(mappedBy = "requestType", fetch = FetchType.LAZY)
  private ApplicationsRequest applicationsRequest;

  @OneToMany(mappedBy = "requestType", fetch = FetchType.LAZY)
  private List<RequestName> requestNames;
}