package com.Code.Compiler.DTO;

import com.Code.Compiler.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithToken {
    private String user;
    private String token;

}
