package com.Code.Compiler.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamplesDTO {
    private Long id;
    private String input;
    private String output;
    private String explanation;
    private Long userId;
}
