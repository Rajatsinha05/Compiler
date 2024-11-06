package com.Code.Compiler.Repository;

import com.Code.Compiler.models.CodeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRequestRepository extends JpaRepository<CodeRequest, String> {
}
