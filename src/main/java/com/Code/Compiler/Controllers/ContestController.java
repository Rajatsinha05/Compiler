package com.Code.Compiler.Controllers;

import com.Code.Compiler.DTO.ContestDTO;
import com.Code.Compiler.DTO.NewContestDTO;
import com.Code.Compiler.Mapper.NewContestMapper;
import com.Code.Compiler.Service.Implementation.ContestServiceImpl;
import com.Code.Compiler.models.Contest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contests")
public class ContestController {
    private static final Logger log = LoggerFactory.getLogger(ContestController.class);
    @Autowired
    private ContestServiceImpl contestService;
    @Autowired
    private final NewContestMapper newContestMapper;

    public ContestController(NewContestMapper newContestMapper) {
        this.newContestMapper = newContestMapper;
    }

    @GetMapping
    public List<ContestDTO> getAllContests() {
        return contestService.getAllContests();
    }

    @GetMapping("/{id}")
    public ContestDTO getContestById(@PathVariable Long id) {
        log.info("Fetching contest with id: {}", id);
        return contestService.getContestById(id)
                .orElseThrow(() -> {
                    log.error("Contest not found with id: {}", id);
                    return new RuntimeException("Contest not found with id: " + id);
                });
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @PostMapping("/contestcreate")
    @ResponseStatus(HttpStatus.CREATED)
    public ContestDTO createContest(@RequestBody ContestDTO contestDTO) {
        return contestService.createContest(contestDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @PutMapping("/{id}")
    public ContestDTO updateContestDetails(@PathVariable Long id, @RequestBody ContestDTO contestDTO) {
        return contestService.updateContestDetails(id, contestDTO);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
    }

    //    creating
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NewContestDTO> creatingContest(@RequestBody NewContestDTO contestDTO) {
        try {
            log.info("Received request to create contest: {}", contestDTO.getTitle());

            // Convert DTO to Entity using the injected mapper instance
            Contest contest = newContestMapper.toEntity(contestDTO);

            // Create contest through service
            Contest createdContest = contestService.creatingContest(contest);

            log.info("Contest '{}' created successfully with ID: {}", createdContest.getTitle(), createdContest.getId());

            // Convert Entity back to DTO for response
            return new ResponseEntity<>(newContestMapper.toDTO(createdContest), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error creating contest: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error occurred while creating contest: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ContestDTO>> getContestsByStudentId(@PathVariable Long studentId) {
        List<ContestDTO> contestList = contestService.getAllContestsByStudentId(studentId);
        return ResponseEntity.ok(contestList);  // Return a 200 OK response with the list of contests
    }
}
