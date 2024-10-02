package com.Code.Compiler.Repository;

import com.Code.Compiler.models.SolvedQuestionInContest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolvedQuestionInContestRepository extends JpaRepository<SolvedQuestionInContest ,Long> {
    SolvedQuestionInContest findByContestIdAndStudentIdAndQuestionId(Long contestId, Long studentId, Long questionId);

    List<SolvedQuestionInContest> findByContestId(Long contestId);

    List<SolvedQuestionInContest> findByStudentIdAndContestId(Long studentId, Long contestId);
}
