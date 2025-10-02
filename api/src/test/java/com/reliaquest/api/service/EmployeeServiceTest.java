package com.reliaquest.api.service;

import com.reliaquest.api.config.EmployeeConfig;
import com.reliaquest.api.model.EmployeeDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeConfig employeeClient;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEmployees_returnsEmptyList_whenClientReturnsNull() {
        when(employeeClient.getAllEmployees()).thenReturn(null);

        List<EmployeeDto> result = employeeService.getAllEmployees();

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void getHighestSalary_returnsMaxSalary_fromClientEmployees() {
        List<EmployeeDto> employees = new ArrayList<>();
        employees.add(new EmployeeDto(UUID.randomUUID(), "A", 50_000, 25, "Jr", "a@x.com"));
        employees.add(new EmployeeDto(UUID.randomUUID(), "B", 75_000, 30, "Mid", "b@x.com"));
        employees.add(new EmployeeDto(UUID.randomUUID(), "C", 60_000, 28, "Jr", "c@x.com"));
        when(employeeClient.getAllEmployees()).thenReturn(employees);

        Integer max = employeeService.getHighestSalary();

        assertThat(max).isEqualTo(75_000);
    }

    @Test
    void getTop10HighestEarningEmployeeNames_sortsAndLimitsTo10() {
        List<EmployeeDto> employees = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            employees.add(new EmployeeDto(UUID.randomUUID(), "Emp" + i, i * 1000, 20 + i, "T", "e@x.com"));
        }
        when(employeeClient.getAllEmployees()).thenReturn(employees);

        List<String> top10 = employeeService.getTop10HighestEarningEmployeeNames();

        assertThat(top10).hasSize(10);
        assertThat(top10.get(0)).isEqualTo("Emp15");
        assertThat(top10.get(9)).isEqualTo("Emp6");
    }

    @Test
    void createEmployee_mapsFields_andReturnsCreated() {
        EmployeeDto input = new EmployeeDto(null, "Jane", 90_000, 30, "Engineer", "jane@x.com");
        EmployeeDto created = new EmployeeDto(UUID.randomUUID(), "Jane", 90_000, 30, "Engineer", "jane@x.com");
        when(employeeClient.createEmployee(any())).thenReturn(created);

        EmployeeDto result = employeeService.createEmployee(input);

        assertThat(result).isEqualTo(created);

        ArgumentCaptor<com.reliaquest.api.model.CreateEmployeeRequest> captor = ArgumentCaptor.forClass(com.reliaquest.api.model.CreateEmployeeRequest.class);
        verify(employeeClient).createEmployee(captor.capture());
        com.reliaquest.api.model.CreateEmployeeRequest sent = captor.getValue();
        assertThat(sent.getName()).isEqualTo("Jane");
        assertThat(sent.getSalary()).isEqualTo(90_000);
        assertThat(sent.getAge()).isEqualTo(30);
        assertThat(sent.getTitle()).isEqualTo("Engineer");
    }

    @Test
    void deleteEmployeeById_usesNameFromLookup_andReturnsName() {
        UUID id = UUID.randomUUID();
        EmployeeDto employee = new EmployeeDto(id, "John", 80_000, 28, "Engineer", "john@x.com");
        when(employeeClient.getEmployeeById(id)).thenReturn(employee);
        when(employeeClient.deleteEmployeeById("John")).thenReturn("John");

        String result = employeeService.deleteEmployeeById(id);

        assertThat(result).isEqualTo("John");
        verify(employeeClient).deleteEmployeeById("John");
    }
}


