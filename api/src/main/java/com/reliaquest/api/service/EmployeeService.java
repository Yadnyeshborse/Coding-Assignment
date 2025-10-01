package com.reliaquest.api.service;

import com.reliaquest.api.config.EmployeeConfig;
import com.reliaquest.api.model.CreateEmployeeRequest;
import com.reliaquest.api.model.EmployeeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;



@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeConfig employeeClient;

    public List<EmployeeDto> getAllEmployees() {
        List<EmployeeDto> employees = employeeClient.getAllEmployees();
        return employees != null ? employees : List.of();
    }

    public List<EmployeeDto> searchEmployeesByName(String name) {
        List<EmployeeDto> employees = employeeClient.getAllEmployees();
        if (employees == null) return List.of();
        final  var result=employees.stream()
                .filter(e -> e.getEmployee_name().toLowerCase().contains(name.toLowerCase()))
                .toList();
        log.info("Found {} employees matching '{}'", result.size(), name);
        return result;
    }

    public EmployeeDto getEmployeeById(UUID id) {
        return employeeClient.getEmployeeById(id);
    }

    public Integer getHighestSalary() {
        List<EmployeeDto> employees = employeeClient.getAllEmployees();
        if (employees == null || employees.isEmpty()) {
            return 0;
        }
        final var highestSalary= employees.stream()
                .map(EmployeeDto::getEmployee_salary)
                .max(Integer::compare).orElse(0);
        log.info("Highest salary: {}", highestSalary);
        return highestSalary;

    }

    public List<String> getTop10HighestEarningEmployeeNames() {
        List<EmployeeDto> employees = employeeClient.getAllEmployees();
        if (employees == null) return List.of();
        final var highestSalary = employees.stream()
                .sorted(Comparator.comparing(EmployeeDto::getEmployee_salary).reversed())
                .limit(10)
                .map(EmployeeDto::getEmployee_name)
                .toList();
        log.info("Top 10 highest earning employees: {}", highestSalary);
        return highestSalary;
    }

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
    CreateEmployeeRequest request = new CreateEmployeeRequest();
    request.setName(employeeDto.getEmployee_name());
    request.setSalary(employeeDto.getEmployee_salary());
    request.setAge(employeeDto.getEmployee_age());
    request.setTitle(employeeDto.getEmployee_title());

    EmployeeDto created = employeeClient.createEmployee(request);
    log.info("Created employee: {}", created);
    return created;
   }


    public String deleteEmployeeById(UUID id) {
        EmployeeDto employee = getEmployeeById(id);
        if (employee == null) return null;
        return employeeClient.deleteEmployeeById(employee.getEmployee_name());
    }



}
