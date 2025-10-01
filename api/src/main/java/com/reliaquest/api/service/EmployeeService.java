package com.reliaquest.api.service;

import com.reliaquest.api.config.EmployeeConfig;
import com.reliaquest.api.model.EmployeeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeConfig employeeClient;

    public List<EmployeeDto> getAllEmployees() {
        List<EmployeeDto> employees = employeeClient.getAllEmployees();
        return employees != null ? employees : List.of();
    }

    public List<EmployeeDto> searchEmployeesByName(String nameFragment) {
        List<EmployeeDto> employees = employeeClient.getAllEmployees();
        if (employees == null) return List.of();
        return employees.stream()
                .filter(e -> e.getEmployee_name().toLowerCase().contains(nameFragment.toLowerCase()))
                .toList();
    }

    public EmployeeDto getEmployeeById(UUID id) {
        return employeeClient.getEmployeeById(id);
    }

    public Integer getHighestSalary() {
        List<EmployeeDto> employees = employeeClient.getAllEmployees();
        if (employees == null || employees.isEmpty()) return 0;
        return employees.stream()
                .map(EmployeeDto::getEmployee_salary)
                .max(Integer::compare)
                .orElse(0);
    }

    public List<String> getTop10HighestEarningEmployeeNames() {
        List<EmployeeDto> employees = employeeClient.getAllEmployees();
        if (employees == null) return List.of();
        return employees.stream()
                .sorted(Comparator.comparing(EmployeeDto::getEmployee_salary).reversed())
                .limit(10)
                .map(EmployeeDto::getEmployee_name)
                .toList();
    }

    public EmployeeDto createEmployee(EmployeeDto input) {
        return employeeClient.createEmployee(input);
    }

    public String deleteEmployeeById(UUID id) {
        EmployeeDto employee = getEmployeeById(id);
        if (employee == null) return null;
        return employeeClient.deleteEmployeeById(employee.getEmployee_name());
    }



}
