package com.reliaquest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.model.EmployeeDto;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeeControllerImpl.class)
class EmployeeControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void getAllEmployees_returnsList() throws Exception {
        Mockito.when(employeeService.getAllEmployees())
                .thenReturn(List.of(new EmployeeDto(UUID.randomUUID(), "A", 1, 20, "T", "a@x.com")));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].employee_name", is("A")));
    }

    @Test
    void createEmployee_returnsCreated() throws Exception {
        EmployeeDto input = new EmployeeDto(null, "Jane", 90000, 30, "Engineer", "jane@x.com");
        EmployeeDto created = new EmployeeDto(UUID.randomUUID(), "Jane", 90000, 30, "Engineer", "jane@x.com");
        Mockito.when(employeeService.createEmployee(any())).thenReturn(created);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name", is("Jane")));
    }

    @Test
    void deleteEmployeeById_returnsName() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(employeeService.deleteEmployeeById(eq(id))).thenReturn("John");

        mockMvc.perform(delete("/api/v1/employees/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string("John"));
    }

    @Test
    void getEmployeesByNameSearch_returnsFilteredList() throws Exception {
        Mockito.when(employeeService.searchEmployeesByName(eq("an")))
                .thenReturn(List.of(
                        new EmployeeDto(UUID.randomUUID(), "Ana", 100, 20, "T", "a@x.com"),
                        new EmployeeDto(UUID.randomUUID(), "Jane", 200, 21, "T", "j@x.com")
                ));

        mockMvc.perform(get("/api/v1/employees/search/an"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].employee_name", is("Ana")))
                .andExpect(jsonPath("$[1].employee_name", is("Jane")));
    }

    @Test
    void getEmployeeById_returns200_whenFound() throws Exception {
        UUID id = UUID.randomUUID();
        EmployeeDto dto = new EmployeeDto(id, "John", 50000, 30, "Engineer", "john@x.com");
        Mockito.when(employeeService.getEmployeeById(eq(id))).thenReturn(dto);

        mockMvc.perform(get("/api/v1/employees/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name", is("John")))
                .andExpect(jsonPath("$.employee_salary", is(50000)))
                .andExpect(jsonPath("$.employee_age", is(30)))
                .andExpect(jsonPath("$.employee_title", is("Engineer")))
                .andExpect(jsonPath("$.employee_email", is("john@x.com")));
    }

    @Test
    void getEmployeeById_returns404_whenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(employeeService.getEmployeeById(eq(id))).thenReturn(null);

        mockMvc.perform(get("/api/v1/employees/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getHighestSalary_returnsValue() throws Exception {
        Mockito.when(employeeService.getHighestSalary()).thenReturn(123456);

        mockMvc.perform(get("/api/v1/employees/highestSalary"))
                .andExpect(status().isOk())
                .andExpect(content().string("123456"));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_returnsList() throws Exception {
        Mockito.when(employeeService.getTop10HighestEarningEmployeeNames())
                .thenReturn(List.of("A", "B", "C"));

        mockMvc.perform(get("/api/v1/employees/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]", is("A")))
                .andExpect(jsonPath("$[1]", is("B")))
                .andExpect(jsonPath("$[2]", is("C")));
    }
}



