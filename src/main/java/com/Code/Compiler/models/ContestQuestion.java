//package com.Code.Compiler.models;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class ContestQuestion  {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "question_id")
//    private Questions question;
//
//    private int marks;
//
//    @ManyToOne
//    @JoinColumn(name = "contest_id")
//    @JsonBackReference
//    private Contest contest;
//
//    public ContestQuestion(Long contestQuestionId) {
//    }
//}

package com.Code.Compiler.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ContestQuestion {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Questions question;

    private int marks;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;
    public ContestQuestion(Long contestQuestionId) {
    }
}

