package com.etnetera.hr.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class JavaScriptFrameworkRequest {
    private String name;
    private String version;
    private String deprecationDate;
    private int hypeLevel;

}
