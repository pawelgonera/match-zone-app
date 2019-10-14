package pl.poul12.matchzone.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.enums.Gender;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface PagingUserRepository extends PagingAndSortingRepository<User, Long> {

    Page<User> findAllByFirstNameStartingWithIgnoreCase(String firstName, Pageable pageable);
    Page<User> findAllByPersonalDetails_Gender(Gender gender, Pageable pageable);
    Page<User> findAllByPersonalDetails_AgeBetween(Integer ageMin, Integer ageMax, Pageable pageable);
    Page<User> findAllByPersonalDetails_RatingBetween(Double ratingMin, Double ratingMax, Pageable pageable);
    Page<User> findAllByPersonalDetails_City(String city, Pageable pageable);


    Page<User> findAllByFirstNameStartingWithIgnoreCaseAndPersonalDetails_GenderAndPersonalDetails_AgeBetween(String firstName, Gender gender, Integer ageMin, Integer ageMax, Pageable pageable);
    Page<User> findAllByFirstNameStartingWithIgnoreCaseAndPersonalDetails_GenderAndPersonalDetails_AgeBetweenAndPersonalDetails_RatingBetween(String firstName, Gender gender, Integer ageMin, Integer ageMax, Double ratingMin, Double ratingMax, Pageable pageable);
    Page<User> findAllByFirstNameStartingWithIgnoreCaseAndPersonalDetails_GenderAndPersonalDetails_AgeBetweenAndPersonalDetails_RatingBetweenAndPersonalDetails_City(@NotEmpty String firstName, @NotEmpty Gender gender, @NotEmpty Integer ageMin, @NotEmpty Integer ageMax, @NotEmpty Double ratingMin, @NotEmpty Double ratingMax, @NotEmpty String city, Pageable pageable);

    Page<User> findAllByFirstNameStartingWithIgnoreCaseAndPersonalDetails_Gender(String firstName, Gender gender, Pageable pageable);
    Page<User> findAllByFirstNameStartingWithIgnoreCaseAndPersonalDetails_AgeBetween(String firstName, Integer ageMin, Integer ageMax, Pageable pageable);
    Page<User> findAllByFirstNameStartingWithIgnoreCaseAndPersonalDetails_RatingBetween(String firstName, Double ratingMin, Double ratingMax, Pageable pageable);
    Page<User> findAllByFirstNameStartingWithIgnoreCaseAndPersonalDetails_City(String firstName, String city, Pageable pageable);

    Page<User> findAllByPersonalDetails_GenderAndPersonalDetails_AgeBetween(Gender gender, Integer ageMin, Integer ageMax, Pageable pageable);
    Page<User> findAllByPersonalDetails_GenderAndPersonalDetails_AgeBetweenAndPersonalDetails_RatingBetween(Gender gender, Integer ageMin, Integer ageMax, Double ratingMin, Double ratingMax, Pageable pageable);
    Page<User> findAllByPersonalDetails_GenderAndPersonalDetails_AgeBetweenAndPersonalDetails_RatingBetweenAndPersonalDetails_City(Gender gender, Integer ageMin, Integer ageMax, Double ratingMin, Double ratingMax, String city, Pageable pageable);
    Page<User> findAllByPersonalDetails_GenderAndPersonalDetails_RatingBetween(Gender gender, Double ratingMin, Double ratingMax, Pageable pageable);
    Page<User> findAllByPersonalDetails_GenderAndPersonalDetails_City(Gender gender,String city, Pageable pageable);

    Page<User> findAllByPersonalDetails_AgeBetweenAndPersonalDetails_RatingBetween(Integer ageMin, Integer ageMax, Double ratingMin, Double ratingMax, Pageable pageable);
    Page<User> findAllByPersonalDetails_AgeBetweenAndPersonalDetails_City(Integer ageMin, Integer ageMax, String city, Pageable pageable);
    Page<User> findAllByPersonalDetails_AgeBetweenAndPersonalDetails_RatingBetweenAndPersonalDetails_City(Integer ageMin, Integer ageMax, Double ratingMin, Double ratingMax, String city, Pageable pageable);


    Page<User> findAllByPersonalDetails_RatingBetweenAndPersonalDetails_City(Double ratingMin, Double ratingMax, String  city, Pageable pageable);

    List<User> findAllByPersonalDetails_Gender(Gender gender, Sort sort);
    List<User> findByPersonalDetailsGender(Gender gender, Sort sort);
}
