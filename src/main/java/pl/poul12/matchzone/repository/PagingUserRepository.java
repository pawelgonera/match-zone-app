package pl.poul12.matchzone.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.enums.Gender;

@Repository
public interface PagingUserRepository extends PagingAndSortingRepository<User, Long> {

    Page<User> findAllByFirstNameStartingWithIgnoreCase(String firstName, Pageable pageable);
    Page<User> findAllByPersonalDetails_Gender(Gender gender, Pageable pageable);
    Page<User> findAllByPersonalDetails_AgeBetween(Integer ageMin, Integer ageMax, Pageable pageable);
    Page<User> findAllByPersonalDetails_City(String city, Pageable pageable);
}
