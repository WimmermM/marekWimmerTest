package com.etnetera.hr.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class FrameworkResponse<T> {

    private int statusCode;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;


    public FrameworkResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}