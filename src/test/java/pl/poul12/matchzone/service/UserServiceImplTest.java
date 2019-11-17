package pl.poul12.matchzone.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import pl.poul12.matchzone.config.ConfigBeans;
import pl.poul12.matchzone.model.Role;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.enums.RoleName;
import pl.poul12.matchzone.repository.RoleRepository;
import pl.poul12.matchzone.repository.UserRepository;
import pl.poul12.matchzone.security.forms.RegisterForm;

import java.util.*;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceImpl.class)
@Import(ConfigBeans.class)
public class UserServiceImplTest {

    private static final User USER_TEST = new User();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Before
    public void setUp(){
        USER_TEST.setId(1L);
        USER_TEST.setUsername("Pablito01");
        USER_TEST.setFirstName("Pablo01");
        USER_TEST.setEmail("mail01@g.com");
        USER_TEST.setPassword("pass01");
    }

    @Test
    public void shouldContainSpecificUserFromAllUsers() {

        //when
        final List<User> userListMock = Collections.singletonList(USER_TEST);
        doReturn(userListMock).when(userRepository).findAll();

        final List<User> userListResult = userService.getAllUsers();

        //then
        assertThat(userListResult, contains(USER_TEST));
    }

    @Test
    public void getAllUsersBySort() {

    }

    @Test
    public void getPageableListOfUsers() {
    }

    @Test
    public void createUser() {
        //given
        RegisterForm registerFormTest = new RegisterForm();
        registerFormTest.setEmail("mail01@g.com");
        registerFormTest.setName("Pablo01");
        registerFormTest.setUsername("Pablito01");
        registerFormTest.setPassword("pass01");
        Set<String> roleSet = new HashSet<>();
        roleSet.add("USER");
        registerFormTest.setRole(roleSet);

        //when
        Role roleTest = new Role();
        roleTest.setId(1L);
        roleTest.setName(RoleName.USER);

        Optional roleOptional = Optional.of(roleTest);
        doReturn(roleOptional).when(roleRepository).findByName(RoleName.USER);

        when(userRepository.save(any(User.class))).thenReturn(USER_TEST);

        final User userSaved = userService.createUser(registerFormTest);

        //then
        assertEquals(USER_TEST, userSaved);
    }

    @Test
    public void shouldReturnUserById() {

        //given
        Optional<User> userOptional = Optional.of(USER_TEST);
        doReturn(userOptional).when(userRepository).findById(USER_TEST.getId());

        //when
        final User userFound = userService.getUserById(1L);

        //then
        assertEquals(userOptional, Optional.of(userFound));
    }

    @Test
    public void shouldReturnUserByUsername() {

        //given
        Optional<User> userOptional = Optional.of(USER_TEST);
        doReturn(userOptional).when(userRepository).findUserByUsername(USER_TEST.getUsername());

        //when
        final User userFound = userService.getUserByUsername(USER_TEST.getUsername());

        //then
        assertEquals(USER_TEST, userFound);
    }

    @Test
    public void shouldReturnUserByEmail() {
        //given
        Optional<User> userOptional = Optional.of(USER_TEST);
        doReturn(userOptional).when(userRepository).findUserByEmail(USER_TEST.getEmail());

        //when
        final User userFound = userService.getUserByEmail(USER_TEST.getEmail());

        //then
        assertEquals(USER_TEST, userFound);
    }

    @Test
    public void shouldGetUpdatedUser() {

        //given
        User userUpdateTest = new User();
        userUpdateTest.setId(1L);
        userUpdateTest.setFirstName("Pablo02");
        userUpdateTest.setUsername("Pablito02");
        userUpdateTest.setEmail("mail02@g.com");
        userUpdateTest.setPassword("pass02");

        //when
        Optional<User> userOptional = Optional.of(userUpdateTest);
        doReturn(userOptional).when(userRepository).findUserByUsername(userUpdateTest.getUsername());

        when(userRepository.save(any(User.class))).thenReturn(userUpdateTest);

        final User userUpdated = userService.updateUser(userUpdateTest.getUsername(), userUpdateTest);

        //then
        assertEquals(userUpdateTest, userUpdated);
    }

    @Test
    public void deleteUser() {
    }

    @Test
    public void filterUserList() {
    }
}