package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
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
    private boolean gender;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "work_status")
    private String workStatus;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "manager_id")
    private String managerId;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "insurance_id")
    private String insuranceId;

    @Column(name = "tax_code")
    private String taxCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
    private IdentityCard identityCard;

    @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
    private WorkingInformation workingInformation;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<RelativeInformation> relativeInformation = new ArrayList<>();
}
