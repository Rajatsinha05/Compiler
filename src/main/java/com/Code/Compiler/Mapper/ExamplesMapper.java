package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.ExamplesDTO;
import com.Code.Compiler.models.Examples;
import com.Code.Compiler.models.User;
import com.Code.Compiler.Repository.UserRepository;

import java.util.Optional;

public class ExamplesMapper {

    public static ExamplesDTO toDTO(Examples example) {
        Long userId = example.getUser() != null ? example.getUser().getId() : null;
        return new ExamplesDTO(
                example.getId(),
                example.getInput(),
                example.getOutput(),
                example.getExplanation(),
                userId
        );
    }

    public static Examples toEntity(ExamplesDTO examplesDTO, UserRepository userRepository) {
        Examples example = new Examples();
        example.setId(examplesDTO.getId());
        example.setInput(examplesDTO.getInput());
        example.setOutput(examplesDTO.getOutput());
        example.setExplanation(examplesDTO.getExplanation());

        // Set the user if userId is present
        if (examplesDTO.getUserId() != null) {
            Optional<User> user = userRepository.findById(examplesDTO.getUserId());
            user.ifPresent(example::setUser);
        }

        return example;
    }
}
