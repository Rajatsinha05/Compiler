package com.Code.Compiler.models;

import com.Code.Compiler.Enum.DifficultLevel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String tags;

    @Enumerated(EnumType.STRING)
    private DifficultLevel difficultLevel;
    private String constraintValue;
    private String input;
    private String expectedOutput;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "question_id")
    @ToString.Exclude
    private List<Examples> examples = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "solvedQuestions")
    @ToString.Exclude
    private List<Students> solvedStudents = new ArrayList<>();
}
