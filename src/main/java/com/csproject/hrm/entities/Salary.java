package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "salary")
public class Salary {
    @Id
    @Column(name = "laudatory_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = WorkingInformation.class)
    @JoinColumn(name = "working_information_id")
    private WorkingInformation workingInformation;

    @Column(name = "final_salary")
    private String finalSalary;

    @Column(name = "subsidize")
    private String subsidize;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}
