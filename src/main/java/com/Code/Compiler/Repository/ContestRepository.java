package com.Code.Compiler.Repository;

import com.Code.Compiler.models.Contest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest,Long> {

    @EntityGraph(attributePaths = {"questions", "enrolledStudents"})
    Optional<Contest> findById(Long id);
}
