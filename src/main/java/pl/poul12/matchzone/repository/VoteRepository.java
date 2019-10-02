package pl.poul12.matchzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.Vote;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findAllByUser(User user);
}
