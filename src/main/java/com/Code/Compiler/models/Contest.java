package com.Code.Compiler.models;

import com.Code.Compiler.Enum.DifficultLevel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalMarks;

    @Enumerated(EnumType.STRING)
    private DifficultLevel difficultyLevel;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToMany
    @JoinTable(
            name = "contest_questions",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Questions> questions;

    @ManyToMany
    @JoinTable(
            name = "contest_enrolled_students",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Students> enrolledStudents;

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContestResult> contestResults;

    public int getStudentScore(Students student) {
        for (ContestResult result : contestResults) {
            if (result.getStudent().equals(student)) {
                return result.getScore();
            }
        }
        return 0;
    }
}
