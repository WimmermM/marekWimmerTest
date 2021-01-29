package com.etnetera.hr.out;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class FrameworkResponse<T> {

    private int statusCode;
    private String message;
    private T data;


    public FrameworkResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
