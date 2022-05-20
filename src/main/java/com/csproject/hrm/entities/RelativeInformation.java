package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "relative_information")
public class RelativeInformation {
    @Id
    @Column(name = "relative_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_name")
    private String parentName;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;
}
