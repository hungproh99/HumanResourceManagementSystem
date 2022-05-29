package com.csproject.hrm.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "education")
public class Education {
    @Id
    @Column(name = "education_id")
    private Long id;

    @Column(name = "name_school")
    private String nameSchool;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "certificate")
    private String certificate;

    @Column(name = "status")
    private String status;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Employee.class)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}