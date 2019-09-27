package pl.poul12.matchzone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Vote;
import pl.poul12.matchzone.service.VoteService;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api/v1")
public class VoteController {

    private VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/vote/{id}")
    public ResponseEntity<Vote> getVoteById(@PathVariable(value = "id") Long voteId) throws ResourceNotFoundException {

        Vote vote = voteService.getUserById(voteId);

        return ResponseEntity.ok().body(vote);
    }

    @PutMapping("/vote/{id}")
    public ResponseEntity<?> updatePersonalDetails(@PathVariable(value = "id") Long userId, @Valid @RequestBody Vote vote) throws ResourceNotFoundException {

        Vote updatedVote =  voteService.updateVote(userId, vote);

        return ResponseEntity.ok(updatedVote);
    }
}
