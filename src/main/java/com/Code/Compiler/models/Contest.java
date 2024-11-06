package com.Code.Compiler.models;

import com.Code.Compiler.Enum.DifficultLevel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Contest {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalMarks = 0;

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

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContestQuestion> contestQuestions;

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContestAttempting> contestAttempts;

    // Helper method to get a student's score from contestResults
    public int getStudentScore(Students student) {
        if (contestResults != null) {
            for (ContestResult result : contestResults) {
                if (result.getStudent().equals(student)) {
                    return result.getTotalScore();
                }
            }
        }
        return 0;
    }
}
