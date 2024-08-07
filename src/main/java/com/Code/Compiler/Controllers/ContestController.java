package com.Code.Compiler.Controllers;

import com.Code.Compiler.DTO.ContestDTO;
import com.Code.Compiler.Service.Implementation.ContestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contests")
public class ContestController {

    @Autowired
    private ContestServiceImpl contestService;

    @GetMapping
    public List<ContestDTO> getAllContests() {
        return contestService.getAllContests();
    }

    @GetMapping("/{id}")
    public ContestDTO getContestById(@PathVariable Long id) {
        return contestService.getContestById(id)
                .orElseThrow(() -> new RuntimeException("Contest not found with id: " + id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @PostMapping
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
}
