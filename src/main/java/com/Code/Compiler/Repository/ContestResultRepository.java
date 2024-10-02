package com.Code.Compiler.Repository;

import com.Code.Compiler.models.ContestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContestResultRepository extends JpaRepository<ContestResult,Long> {
    Optional<ContestResult> findByContestIdAndStudentId(Long contestId, Long studentId);

}
