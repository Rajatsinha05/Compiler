package com.Code.Compiler.Repository;

import com.Code.Compiler.models.Contest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest,Long> {

    @Query("SELECT c FROM Contest c WHERE c.id = :id")
    Optional<Contest> findById(@Param("id") Long id);
}
