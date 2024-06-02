package com.Code.Compiler.Controllers;

import com.Code.Compiler.Service.Implementation.ContestServiceImpl;
import com.Code.Compiler.models.Contest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contest createContest(@RequestBody Contest contest, @RequestParam Long userId) {
        return contestService.createContest(contest, userId);
    }

    @PutMapping("/{id}")
    public Contest updateContestDetails(@PathVariable Long id, @RequestBody Contest contestDetails) {
        return contestService.updateContestDetails(id, contestDetails);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
    }
}

