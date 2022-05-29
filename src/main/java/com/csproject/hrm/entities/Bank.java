package com.csproject.hrm.entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "bank")
public class Bank {
	@Id
	@Column(name = "bank_id")
	private Long id;
	
	@Column(name = "name_bank")
	private String nameBank;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	@Column(name = "account_name")
	private String accountName;
	
	@OneToOne(mappedBy = "bank", fetch = FetchType.LAZY)
	private Employee employee;
}