package com.Code.Compiler.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeRequest {
    private String code;
    private String language;
    private  String inputData;
}
