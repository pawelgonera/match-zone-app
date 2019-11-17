package pl.poul12.matchzone.service;

import org.springframework.stereotype.Service;
import pl.poul12.matchzone.controller.Rating;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.Vote;
import pl.poul12.matchzone.repository.UserRepository;
import pl.poul12.matchzone.repository.VoteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    private VoteRepository voteRepository;
    private UserRepository userRepository;
    private UserServiceImpl userServiceImpl;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository, UserServiceImpl userServiceImpl) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.userServiceImpl = userServiceImpl;
    }

    public Vote getVoteById(Long id)  {

        Optional<Vote> voteFound = voteRepository.findById(id);

        return voteFound.orElseThrow(() -> new ResourceNotFoundException("Vote not found for this id: " + id)
        );
    }

    public List<Vote> getAll(){

        return voteRepository.findAll();
    }

    public Vote createVote(String username, Vote vote){

        User user = userServiceImpl.getUserByUsername(username);

        vote.setUser(user);

        return voteRepository.save(vote);
    }

    public Rating getRatingInfo(String username) {
        User user = userServiceImpl.getUserByUsername(username);
        List<Vote> votes = voteRepository.findAllByUser(user);

        Long countedVotes = votes.stream()
                                 .mapToLong(Vote::getId)
                                 .count();
        Double sumOfVotes = votes.stream()
                                 .mapToDouble(Vote::getValue)
                                 .sum();

        Rating rating = new Rating();
        rating.setCountedVotes(countedVotes);
        rating.setSumOfVotes(sumOfVotes);

        return rating;
    }

    public Boolean checkIfLoggedUserVoted(String  username, String usernameLogged) {
        User user = userServiceImpl.getUserByUsername(username);
        List<Vote> votes = voteRepository.findAllByUser(user);
        return votes.stream()
                    .map(Vote::getAuthor)
                    .anyMatch(author -> author.equals(usernameLogged));
    }

}
