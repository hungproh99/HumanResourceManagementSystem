package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "working_information")
public class WorkingInformation {
    @Id
    @Column(name = "working_information_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(table = "working_information")
    private String recentPromotion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "area_id")
    private Area area;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "office_id")
    private Office office;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_id")
    private Job job;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contract_id")
    private WorkContract workContract;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
