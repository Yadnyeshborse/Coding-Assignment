package com.reliaquest.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseEmp<T> {
    private T data;
    private String status;
    private String error;

    public ResponseEmp(T data, Status status, String error) {
        this.data = data;
        this.status = status.getValue();
        this.error = error;
    }

    public static <T> ResponseEmp<T> handledWithEmp(T data) {
        return new ResponseEmp<>(data, Status.HANDLED, null);
    }

    public static <T> ResponseEmp<T> errorEmp(String error) {
        return new ResponseEmp<>(null, Status.ERROR, error);
    }

    public T getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public enum Status {
        HANDLED("Successfully processed Employee request."),
        ERROR("Failed to processed Employee request.");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}

