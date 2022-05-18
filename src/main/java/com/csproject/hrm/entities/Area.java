package com.csproject.hrm.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "area")
public class Area {
    @Id
    @Column(name = "area_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "area", fetch = FetchType.LAZY)
    private WorkingInformation workingInformation;
}
