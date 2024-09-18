package com.Code.Compiler.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"contest", "question"}) // Avoid recursion in toString
public class ContestQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    @JsonBackReference // Prevent recursion
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @JsonManagedReference // This side will be serialized
    private Questions question;

    private int marks;
}
