package com.csproject.hrm.repositories;

import com.csproject.hrm.entities.Demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface DemoRepository  extends JpaRepository<Demo, Long>{
	
	String Q_FIND_DEMO_BY_ID = "SELECT d FROM demo d LEFT JOIN FETCH d.demo2 v WHERE d.id = ?";
	
	
	@Query(Q_FIND_DEMO_BY_ID)
	Demo getDemoByID(Long id);
}