package com.Code.Compiler.Repository;

import com.Code.Compiler.models.Contest;
import com.Code.Compiler.models.ContestQuestion;
import com.Code.Compiler.models.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestQuestionRepository extends JpaRepository<ContestQuestion ,Long> {
    List<ContestQuestion> findByContestId(Long id);

    ContestQuestion findByContestAndQuestion(Contest contest, Questions question);
}
