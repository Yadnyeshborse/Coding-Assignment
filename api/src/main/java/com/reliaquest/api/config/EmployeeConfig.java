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
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class EmployeeConfig {

    @Autowired
    private RestTemplate restTemplate;

    //base url
    @Value("${employee.client.base-url}")
    private String baseUrl;

    public List<EmployeeDto> getAllEmployees() {
        ResponseWrapper<List<EmployeeDto>> response =
                restTemplate.exchange(baseUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<ResponseWrapper<List<EmployeeDto>>>() {}).getBody();
        return response != null ? response.getData() : null;
    }

    public EmployeeDto getEmployeeById(UUID id) {
        ResponseWrapper<EmployeeDto> response =
                restTemplate.exchange(baseUrl + "/" + id, HttpMethod.GET, null,
                        new ParameterizedTypeReference<ResponseWrapper<EmployeeDto>>() {}).getBody();
        return response != null ? response.getData() : null;
    }

    public EmployeeDto createEmployee(CreateEmployeeRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateEmployeeRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseWrapper<EmployeeDto> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<ResponseWrapper<EmployeeDto>>() {}
            ).getBody();

            return response != null ? response.getData() : null;
        } catch (HttpStatusCodeException ex) {
            throw new ResponseStatusException(ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
        }
    }

    public String deleteEmployeeById(String name) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<DeleteEmployeeRequest> entity = new HttpEntity<>(new DeleteEmployeeRequest(name), headers);
            restTemplate.exchange(
                    baseUrl,
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
