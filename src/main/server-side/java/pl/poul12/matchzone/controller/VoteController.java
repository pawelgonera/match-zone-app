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
    public ResponseEntity<Vote> getVoteById(@PathVariable(value = "id") Long voteId) {

        Vote vote = voteService.getVoteById(voteId);

        return ResponseEntity.ok().body(vote);
    }

    @GetMapping("/votes")
    public List<Vote> getAll(){

        return voteService.getAll();
    }

    @PostMapping("/votes/{username}")
    public ResponseEntity<?> addVote(@PathVariable(value = "username") String username, @RequestBody Vote vote) {

        voteService.createVote(username, vote);

        return new ResponseEntity<>(new CustomErrorResponse("Vote created successfully"), HttpStatus.OK);
    }

    @GetMapping("/votes/rating-info/{username}")
    public ResponseEntity<Rating> getRatingInfo(@PathVariable(value = "username") String username) {
        Rating rating = voteService.getRatingInfo(username);
        return ResponseEntity.ok().body(rating);
    }

    @GetMapping("/votes/is-voted/{username}/{usernameLogged}")
    public Boolean checkIfLoggedUserVoted(@PathVariable(value = "username") String username, @PathVariable(value = "usernameLogged") String usernameLogged) {
        Boolean isVoted = voteService.checkIfLoggedUserVoted(username, usernameLogged);
        logger.info("IsVoted: {}", isVoted);
        return voteService.checkIfLoggedUserVoted(username, usernameLogged);
    }

}
