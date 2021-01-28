package com.etnetera.hr.out;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class FrameworkResponse<T> {

    private HttpStatus status;
    private String message;
    private T data;


    public FrameworkResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
