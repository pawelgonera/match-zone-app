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
import java.util.stream.Collectors;

@Service
public class VoteService {

    private VoteRepository voteRepository;
    private UserRepository userRepository;
    private UserService userService;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository, UserService userService) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Vote getVoteById(Long id) throws ResourceNotFoundException {

        Optional<Vote> voteFound = voteRepository.findById(id);

        return voteFound.orElseThrow(() -> new ResourceNotFoundException("Vote not found for this id: " + id)
        );
    }

    public List<Vote> getAll(){

        return voteRepository.findAll();
    }

    public Vote createVote(Long userId, Vote vote) throws ResourceNotFoundException {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User no found for this id: " + userId));

        vote.setUser(user);

        return voteRepository.save(vote);
    }

    public Rating getRatingInfo(Long userId) throws ResourceNotFoundException {
        User user = userService.getUserById(userId);
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

    public Boolean checkIfLoggedUserVoted(Long userId, String usernameLogged) throws ResourceNotFoundException {
        User user = userService.getUserById(userId);
        List<Vote> votes = voteRepository.findAllByUser(user);
        return votes.stream()
                    .map(Vote::getAuthor)
                    .anyMatch(author -> author.equals(usernameLogged));
    }

    /*public Vote updateVote(Long userId, Vote vote) throws ResourceNotFoundException {

        Vote voteFound = voteRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Vote not found for this id: " + userId));

        voteFound.setCountedVotes(vote.getCountedVotes());
        voteFound.setRating(vote.getRating());
        voteFound.setSumOfVotes(vote.getSumOfVotes());

        return voteRepository.save(voteFound);
    }*/
}
