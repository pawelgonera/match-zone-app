package pl.poul12.matchzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.enums.Gender;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);

    List<User> findAllByFirstNameStartingWithIgnoreCase(String firstName);
    List<User> findAllByPersonalDetails_Gender(Gender gender);
    List<User> findAllByPersonalDetails_AgeBetween(Integer ageMin, Integer ageMax);
    List<User> findAllByPersonalDetails_City(String city);

    //List<User> findAllByFirstNameStartingWithIgnoreCaseOrPersonalDetails_GenderOrPersonalDetails_AgeBetweenOrPersonalDetails_City(
            //String firstName, Gender gender,Integer ageMin, Integer ageMax, String city);
}
