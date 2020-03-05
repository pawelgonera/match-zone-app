package pl.poul12.matchzone.service;

import pl.poul12.matchzone.controller.Rating;
import pl.poul12.matchzone.model.Vote;

import java.util.List;

public interface VoteService {

    Vote getVoteById(Long id);

    List<Vote> getAll();

    Vote createVote(String username, Vote vote);

    Rating getRatingInfo(String username);

    Boolean checkIfLoggedUserVoted(String  username, String usernameLogged);
}
