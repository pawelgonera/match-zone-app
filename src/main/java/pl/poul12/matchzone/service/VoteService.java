package pl.poul12.matchzone.service;

import org.springframework.stereotype.Service;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Vote;
import pl.poul12.matchzone.repository.VoteRepository;

import java.util.Optional;

@Service
public class VoteService {

    private VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote getUserById(Long id) throws ResourceNotFoundException {

        Optional<Vote> voteFound = voteRepository.findByUserId(id);

        return voteFound.orElseThrow(() -> new ResourceNotFoundException("Vote not found for this id: " + id)
        );
    }

    public Vote updateVote(Long userId, Vote vote) throws ResourceNotFoundException {

        Vote voteFound = voteRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Vote not found for this id: " + userId));

        voteFound.setCountedVotes(vote.getCountedVotes());
        voteFound.setRating(vote.getRating());
        voteFound.setSumOfVotes(vote.getSumOfVotes());

        return voteRepository.save(voteFound);
    }
}
