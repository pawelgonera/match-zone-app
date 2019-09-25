package pl.poul12.matchzone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Appearance;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.repository.AppearanceRepository;
import pl.poul12.matchzone.repository.PersonalDetailsRepository;
import pl.poul12.matchzone.repository.UserRepository;
import pl.poul12.matchzone.security.forms.RegisterForm;

import java.io.IOException;
import java.util.*;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers(){

        return userRepository.findAll();
    }

    public User createUser(RegisterForm registerUser){

        User user = buildUser(registerUser);
        user.setRole(registerUser.getRole());

        return userRepository.save(user);
    }

    public User getUserById(Long id) throws ResourceNotFoundException {

        Optional<User> userFound = userRepository.findById(id);

        return userFound.orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + id)
        );
    }

    public Optional<User> getUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findUserByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) throws UsernameNotFoundException {

        return userRepository.findUserByEmail(email);
    }

    public ResponseEntity<String> savePhoto(Long id, MultipartFile file) throws ResourceNotFoundException {

        User user = getUserById(id);

        try {
            user.getPersonalDetails().setPhoto(file.getBytes());
            userRepository.save(user);
            logger.info("Photo uploaded");
            return ResponseEntity.status(HttpStatus.OK).body("You successfully uploaded " + file.getOriginalFilename() + "!");
        }catch (IOException e){
            logger.error("Something went wrong with your file: {}", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Something went wrong with your file" + file.getOriginalFilename());
        }
    }

    public User updateUser(Long id, User user) throws ResourceNotFoundException {

        //User userFound = getUserById(id);

        return userRepository.save(user);
    }

    public Map<String, Boolean> deleteUser(Long id) throws ResourceNotFoundException {

        User user = getUserById(id);

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


    private User buildUser(RegisterForm registerUser){

        User user = new User();
        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setFirstName(registerUser.getName());
        personalDetails.setDateOfBirth(registerUser.getDateOfBirth());
        user.setPersonalDetails(personalDetails);
        user.setUsername(registerUser.getUsername());
        user.setEmail(registerUser.getEmail());
        user.setPassword(passwordEncoder.encode(registerUser.getPassword()));

        return user;
    }


}
