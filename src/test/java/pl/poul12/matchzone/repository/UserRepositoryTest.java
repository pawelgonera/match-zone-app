package pl.poul12.matchzone.repository;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import pl.poul12.matchzone.MatchZoneApplication;
import pl.poul12.matchzone.config.H2DbConfig;
import pl.poul12.matchzone.model.Comment;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.enums.Gender;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MatchZoneApplication.class, H2DbConfig.class})
@SqlGroup({
        @Sql(scripts = "/test_data.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(scripts = "/drop_data.sql", executionPhase = AFTER_TEST_METHOD)
})
public class UserRepositoryTest {

    private final static Sort DEFAULT_SORT = Sort.by(Sort.DEFAULT_DIRECTION, "firstName");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void shouldGetCommentByAuthor(){
        //given
        String usernameTest = "Pablito01";

        //when
        List<Comment> comments = commentRepository.findByAuthor(usernameTest);

        //then
        assertEquals(usernameTest, comments.get(0).getAuthor());
    }

    @Test
    public void shouldGetUserFromDatabaseByUsername() {
        //given
        String usernameTest = "Pablito01";

        //when
        User userFound = userRepository.findUserByUsername(usernameTest).orElseThrow(this::exceptionHandler);

        //then
        assertEquals(usernameTest, userFound.getUsername());
    }

    @Test
    public void shouldGetUserFromDatabaseByEmail() {
        //given
        String emailTest = "mail01@g.com";

        //when
        User userFound = userRepository.findUserByEmail(emailTest).orElseThrow(this::exceptionHandler);

        //then
        assertEquals(emailTest, userFound.getEmail());
    }

    @Test
    public void shouldGetAllUsersByFirstNameStartingWithIgnoreCase() {
        //given
        String firstNameTest = "pablo";
        int numberOfUserList = 10;

        //when
        List<User> userListFound = userRepository.findAllByFirstNameStartingWithIgnoreCase(firstNameTest, DEFAULT_SORT);

        //then
        assertEquals(numberOfUserList, userListFound.size());
    }

    @Test
    public void shouldGetAllUsersByGender() {
        //given
        Gender gender = Gender.MALE;
        int numberOfUserList = 5;

        //when
        List<User> userListFound = userRepository.findAllByPersonalDetails_Gender(gender, DEFAULT_SORT);

        //then
        assertEquals(numberOfUserList, userListFound.size());
    }

    @Test
    public void shouldGetAllByUsersByAgeBetweenRange() {
        //given
        Integer ageMin = 18;
        Integer ageMax = 27;
        int numberOfUserList = 10;

        //when
        List<User> userListFound = userRepository.findAllByPersonalDetails_AgeBetween(ageMin, ageMax, DEFAULT_SORT);

        //then
        assertEquals(numberOfUserList, userListFound.size());
    }

    @Test
    public void shouldGetAllByUsersByRatingBetweenRange() {
        //given
        Double ratingMin = 1.0;
        Double ratingMax = 6.0;
        int numberOfUserList = 10;

        //when
        List<User> userListFound = userRepository.findAllByPersonalDetails_RatingBetween(ratingMin, ratingMax, DEFAULT_SORT);

        //then
        assertEquals(numberOfUserList, userListFound.size());
    }

    @Test
    public void shouldGetAllByUsersByCity() {
        //given
        String city = "Berlin";
        int numberOfUserList = 3;

        //when
        List<User> userListFound = userRepository.findAllByPersonalDetails_City(city, DEFAULT_SORT);

        //then
        assertEquals(numberOfUserList, userListFound.size());
    }


    public RuntimeException exceptionHandler(){
        throw new UsernameNotFoundException("Not found that user");
    }
}