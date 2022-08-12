package com.csproject.hrm.services;

import com.csproject.hrm.entities.Employee;
import com.csproject.hrm.exception.CustomErrorException;
import com.csproject.hrm.jwt.UserDetailsImpl;
import com.csproject.hrm.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsService
    implements org.springframework.security.core.userdetails.UserDetailsService {
  @Autowired private EmployeeRepository employeeRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    Optional<Employee> employee = employeeRepository.findById(id);
    if (employee.isEmpty()) {
      throw new UsernameNotFoundException("User not found with id: " + id);
    }
    if (!employee.get().getWorkingStatus()) {
      throw new CustomErrorException("User was deactive with id: " + id);
    }
    return UserDetailsImpl.build(employee.get());
  }
}
