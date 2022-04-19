package com.csproject.hrm.services.impl;


import com.csproject.hrm.entities.Demo;
import com.csproject.hrm.repositories.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoService{
	
	private final DemoRepository demoRepository;
	
	@Autowired
	public DemoService(DemoRepository demoRepository) {this.demoRepository = demoRepository;}
	
	public Demo getDemoByID(Long id) {
		if(id <= 0){
			throw new IllegalArgumentException();
		}
		return demoRepository.getDemoByID(id);
	}
}