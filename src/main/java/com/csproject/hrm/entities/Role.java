package com.csproject.hrm.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private ERole ERole;

    @ManyToMany(mappedBy = "role")
    private List<Employee> employee;
}
