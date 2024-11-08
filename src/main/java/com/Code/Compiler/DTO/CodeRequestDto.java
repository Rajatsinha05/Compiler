package com.Code.Compiler.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeRequestDto {
    private String requestId; // Changed from `id` to `requestId`
    private String code;
    private String language;
    private String inputData;
    private Long userId; // Assuming this is needed for processing
}
