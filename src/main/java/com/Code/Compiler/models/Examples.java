package com.Code.Compiler.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Examples {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String input;
    private String output;
    private String explanation;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
