package com.csproject.hrm.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Builder
@Entity
@Table(name = "work_contract")
public class WorkContract {
    @Id
    @Column(name = "contract_id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "status")
    private String status;

    @Column(name = "representative")
    private String representative;

    @OneToOne(mappedBy = "workContract", fetch = FetchType.LAZY)
    private WorkingInformation workingInformation;
//    @Column
//    private Salary salary;
}
