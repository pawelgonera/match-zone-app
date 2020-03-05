package pl.poul12.matchzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poul12.matchzone.model.PersonalDetails;

import java.util.Optional;

public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Long> {

    Optional<PersonalDetails> findByUserId(Long userId);

}
