package com.Code.Compiler.Controllers;

import com.Code.Compiler.Service.Implementation.ContestServiceImpl;
import com.Code.Compiler.models.Contest;
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
    public List<Contest> getAllContests() {
        return contestService.getAllContests();
    }

    @GetMapping("/{id}")
    public Contest getContestById(@PathVariable Long id) {
        return contestService.getContestById(id)
                .orElseThrow(() -> new RuntimeException("Contest not found with id: " + id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Contest createContest(@PathVariable Long userId ,@RequestBody Contest contest) {
        return contestService.createContest(contest, userId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @PutMapping("/{id}")
    public Contest updateContestDetails(@PathVariable Long id, @RequestBody Contest contestDetails) {
        return contestService.updateContestDetails(id, contestDetails);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
    }
}
