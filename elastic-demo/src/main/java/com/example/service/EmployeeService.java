package com.example.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Employee;
import com.example.repository.EmployeeRepository;

@Service
@Transactional(rollbackOn = { Throwable.class })
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public List<Employee> findAll() {
		return (List<Employee>) employeeRepository.findAll();
	}

	public Employee create(Employee employee) {
		return employeeRepository.save(employee);
	}

}
