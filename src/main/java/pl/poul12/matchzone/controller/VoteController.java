package pl.poul12.matchzone.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Vote;
import pl.poul12.matchzone.service.VoteService;
import pl.poul12.matchzone.util.CustomErrorResponse;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api/v1")
public class VoteController {

    private static final Logger logger = LoggerFactory.getLogger(VoteController.class);

    private VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/votes/{id}")
    public ResponseEntity<Vote> getVoteById(@PathVariable(value = "id") Long voteId) throws ResourceNotFoundException {

        Vote vote = voteService.getVoteById(voteId);

        return ResponseEntity.ok().body(vote);
    }

    @GetMapping("/votes")
    public List<Vote> getAll(){

        return voteService.getAll();
    }

    @PostMapping("/votes/{id}")
    public ResponseEntity<?> addVote(@PathVariable(value = "id") Long userId, @RequestBody Vote vote) throws ResourceNotFoundException {

        voteService.createVote(userId, vote);

        return new ResponseEntity<>(new CustomErrorResponse("Vote created successfully"), HttpStatus.OK);
    }

    @GetMapping("/votes/rating-info/{id}")
    public ResponseEntity<Rating> getRatingInfo(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        Rating rating = voteService.getRatingInfo(userId);
        return ResponseEntity.ok().body(rating);
    }

    @GetMapping("/votes/is-voted/{id}/{username}")
    public Boolean checkIfLoggedUserVoted(@PathVariable(value = "id") Long userId, @PathVariable(value = "username") String usernameLogged) throws ResourceNotFoundException {
        Boolean isVoted = voteService.checkIfLoggedUserVoted(userId, usernameLogged);
        logger.info("IsVoted: {}", isVoted);
        return voteService.checkIfLoggedUserVoted(userId, usernameLogged);
    }

    /*@PutMapping("/vote/{id}")
    public ResponseEntity<?> updatePersonalDetails(@PathVariable(value = "id") Long userId, @Valid @RequestBody Vote vote) throws ResourceNotFoundException {

        Vote updatedVote =  voteService.updateVote(userId, vote);

        return ResponseEntity.ok(updatedVote);
    }*/
}
