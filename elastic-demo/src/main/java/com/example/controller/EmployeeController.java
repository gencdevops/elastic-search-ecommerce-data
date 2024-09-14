package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Employee;
import com.example.repository.es.EmployeeESRepo;
import com.example.service.EmployeeService;

@RestController
@Validated
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeESRepo employeeEsRepo;

	@RequestMapping("/getAll")
	public List<Employee> getEmployees() {
		List<Employee> employeesList;
		/*employeesList = new ArrayList<Employee>();
		employeesList.add(new Employee(1, "lokesh", "gupta", "lokesh@gmail.com"));
		*/
		/*employeesList = employeeService.findAll();*/
		employeesList = employeeEsRepo.searchAll();

		return employeesList;
	}

	@RequestMapping("/get/{id}")
	public Employee getEmployeeById(@PathVariable("id") Long id) {
		return employeeEsRepo.searchById(id);
	}
	
	@RequestMapping("/getByName/{name}")
	public List<Employee> getEmployeeByName(@PathVariable("name") String name) {
		return employeeEsRepo.searchByName(name);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Employee addEmployee(@RequestBody Employee employee) {
		employee = employeeService.create(employee);
		employeeEsRepo.insert(employee, employee.getId());
		return employee;
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public Employee updateEmployee(@RequestBody Employee employee, @PathVariable("id") Long id) {
		employeeEsRepo.update(employee, id);
		return getEmployeeById(id);
	}

	@RequestMapping(value = "/delete/{id}")
	public boolean deleteEmployee(@PathVariable("id") Long id) {
		employeeEsRepo.delete(id);
		return true;
	}

}
