package com.mycompany.myapp.service.dto;

public class ResponseEntityDTO<T> {

    T object;

    String message;

    String error;

    int statusCode;

    public ResponseEntityDTO() {}

    public ResponseEntityDTO(T object, String message, String error, int statusCode) {
        this.object = object;
        this.message = message;
        this.error = error;
        this.statusCode = statusCode;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
