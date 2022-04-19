package com.csproject.hrm.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "Demo2")
public class Demo2 {
	@Id
	@Column(name = "id", nullable = false)
	private Long id;
	
	@OneToMany(mappedBy = "demo", fetch = FetchType.LAZY)
	@Cascade(value = {CascadeType.ALL})
	@JsonIgnore
	@ToString.Exclude
	private Set<Demo> injectionSchedules;
}