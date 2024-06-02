package com.Code.Compiler.Repository;

import com.Code.Compiler.models.Examples;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExampleRepository extends JpaRepository<Examples ,Long> {
}
