package pl.poul12.matchzone.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.enums.Gender;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);

    @Query(value = "SELECT u FROM User u WHERE UPPER(u.firstName) LIKE UPPER(CONCAT(:firstName, '%'))")
    List<User> findAllByFirstNameStartingWithIgnoreCase(@Param("firstName") String firstName, Sort sort);
    List<User> findAllByPersonalDetails_Gender(Gender gender, Sort sort);
    List<User> findAllByPersonalDetails_AgeBetween(Integer ageMin, Integer ageMax, Sort sort);
    List<User> findAllByPersonalDetails_RatingBetween(Double ratingMin, Double ratingMax, Sort sort);
    List<User> findAllByPersonalDetails_City(String city, Sort sort);
}
