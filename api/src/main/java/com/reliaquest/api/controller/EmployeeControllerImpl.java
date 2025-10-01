package com.reliaquest.api.controller;

import com.reliaquest.api.model.EmployeeDto;
import com.reliaquest.api.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeControllerImpl implements IEmployeeController<EmployeeDto, EmployeeDto> {

    private final EmployeeService employeeService;

    @Override
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByNameSearch(@PathVariable String searchString) {
        return ResponseEntity.ok(employeeService.searchEmployeesByName(searchString));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        EmployeeDto employee = employeeService.getEmployeeById(uuid);
        return employee != null ? ResponseEntity.ok(employee) : ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(employeeService.getHighestSalary());
    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.ok(employeeService.getTop10HighestEarningEmployeeNames());
    }

    @Override
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeInput) {
        EmployeeDto created = employeeService.createEmployee(employeeInput);
        return ResponseEntity.ok(created);
    }

//    @Override
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
//        String deletedName = employeeService.deleteEmployeeById(UUID.fromString(id));
//        return ResponseEntity.ok(deletedName);
//    }
}
