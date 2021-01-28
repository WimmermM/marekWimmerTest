package com.etnetera.hr.in;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JavaScriptFrameworkRequest {
    private Long id;
    private String version;
    private String deprecationDate;
    private int hypeLevel;
}
