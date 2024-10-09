package com.Code.Compiler.Repository;

import com.Code.Compiler.models.Contest;
import com.Code.Compiler.models.ContestAttempting;
import com.Code.Compiler.models.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestAttemptingRepository extends JpaRepository<ContestAttempting,Long> {
    Optional<ContestAttempting> findByContestAndStudentAndEndTimeIsNull(Contest contest, Students student);
    List<ContestAttempting> findByContest(Contest contest);
}

