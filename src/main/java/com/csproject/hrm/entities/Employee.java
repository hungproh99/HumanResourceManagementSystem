package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employee")
public class Employee {
  @Id
  @Column(name = "employee_id")
  private String id;

  @Column(name = "personal_email")
  @Email
  private String personalEmail;

  @Column(name = "company_email")
  @Email
  private String companyEmail;

  @Column(name = "password")
  private String password;

  @Column(name = "fullName")
  private String fullName;

  @Column(name = "gender")
  private String gender;

  @Column(name = "address")
  private String address;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Column(name = "marital_status")
  private String maritalStatus;

  @Column(name = "working_status")
  @Type(type = "boolean")
  private Boolean workingStatus;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "role_type")
  private RoleType roleType;

  @Column(name = "manager_id")
  private String managerId;

  @Column(name = "avatar")
  private String avatar;

  @Column(name = "nick_name")
  private String nickName;

  @Column(name = "facebook")
  private String facebook;

  @Column(name = "tax_code")
  private String taxCode;

  @Column(name = "current_situation")
  private String currentSituation;

  @Column(name = "level")
  private int level;

  @Column(name = "create_date")
  private LocalDateTime createDate;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "card_id")
  private IdentityCard identityCard;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private List<WorkingContract> workingContract;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private List<WorkingHistory> workingHistory;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private List<RelativeInformation> relativeInformation;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private List<Education> educations;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "bank_id")
  private Bank bank;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "employee_type_id")
  private EmployeeType employeeType;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "working_type_id")
  private WorkingType workingType;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private List<Timekeeping> timekeepings;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private List<ApplicationsRequest> applicationsRequest;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private List<EmployeeInsurance> employeeInsuranceList;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private List<EmployeeTax> employeeTaxList;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private List<EmployeeAllowance> employeeAllowanceList;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private List<Area> area;
}
