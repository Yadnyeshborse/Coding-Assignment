package com.reliaquest.api.config;

import com.reliaquest.api.model.EmployeeDto;
import com.reliaquest.api.model.ResponseWrapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class EmployeeConfig {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8112/api/v1/employee";

    public List<EmployeeDto> getAllEmployees() {
        ResponseWrapper<List<EmployeeDto>> response =
                restTemplate.exchange(BASE_URL, HttpMethod.GET, null,
                        new ParameterizedTypeReference<ResponseWrapper<List<EmployeeDto>>>() {}).getBody();
        return response != null ? response.getData() : null;
    }

    public EmployeeDto getEmployeeById(UUID id) {
        ResponseWrapper<EmployeeDto> response =
                restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.GET, null,
                        new ParameterizedTypeReference<ResponseWrapper<EmployeeDto>>() {}).getBody();
        return response != null ? response.getData() : null;
    }

    public EmployeeDto createEmployee(EmployeeDto input) {
        ResponseWrapper<EmployeeDto> response =
                restTemplate.exchange(
                        BASE_URL,
                        HttpMethod.POST,
                        new org.springframework.http.HttpEntity<>(input),
                        new ParameterizedTypeReference<ResponseWrapper<EmployeeDto>>() {}).getBody();
        return response != null ? response.getData() : null;
    }


    public String deleteEmployeeById(String name) {
        restTemplate.delete(BASE_URL + "/" + name);
        return name;
    }
}
