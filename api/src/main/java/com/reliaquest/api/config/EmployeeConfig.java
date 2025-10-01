package com.reliaquest.api.config;

import com.reliaquest.api.model.CreateEmployeeRequest;
import com.reliaquest.api.model.EmployeeDto;
import com.reliaquest.api.model.DeleteEmployeeRequest;
import com.reliaquest.api.model.ResponseWrapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;
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

    public EmployeeDto createEmployee(CreateEmployeeRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateEmployeeRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseWrapper<EmployeeDto> response = restTemplate.exchange(
                    BASE_URL,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<ResponseWrapper<EmployeeDto>>() {}
            ).getBody();

            return response != null ? response.getData() : null;
        } catch (HttpStatusCodeException ex) {
            // Propagate upstream server status (e.g., 400 for validation errors) instead of masking as 500
            throw new ResponseStatusException(ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
        }
    }


//public EmployeeDto createEmployee(EmployeeDto input) {
//    try {
//        var request = new org.springframework.http.HttpEntity<>(input);
//        var response = restTemplate.exchange(
//                BASE_URL,
//                HttpMethod.POST,
//                request,
//                new ParameterizedTypeReference<ResponseWrapper<EmployeeDto>>() {}
//        ).getBody();
//
//        if (response != null) {
//            return response.getData();
//        } else {
//            throw new RuntimeException("Empty response from backend when creating employee");
//        }
//    } catch (Exception e) {
//        // Log the exact exception to help debug
//        e.printStackTrace();
//        throw new RuntimeException("Failed to create employee: " + e.getMessage(), e);
//    }
//}



    public String deleteEmployeeById(String name) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<DeleteEmployeeRequest> entity = new HttpEntity<>(new DeleteEmployeeRequest(name), headers);
            restTemplate.exchange(
                    BASE_URL,
                    HttpMethod.DELETE,
                    entity,
                    new ParameterizedTypeReference<ResponseWrapper<Boolean>>() {}
            );
            return name;
        } catch (HttpStatusCodeException ex) {
            throw new ResponseStatusException(ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
        }
    }
}
