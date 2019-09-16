package pl.poul12.matchzone.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.repository.UserRepository;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/v1")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/users/{id}/changeavatar")
    public ResponseEntity<String> changeAvatar(@PathVariable(value = "id") Long userId, @RequestParam("file")MultipartFile file) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));

        try {
            user.getPersonalDetails().setPhoto(file.getBytes());
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body("You successfully uploaded " + file.getOriginalFilename() + "!");
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Something went wrong with your file " + file.getOriginalFilename());
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetails) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));

        final User updatedUser = userRepository.save(setUpdatedUser(user, userDetails));
        return ResponseEntity.ok(updatedUser);
    }

    /*@PostMapping("users/upload-file")
    public Blob uploadFile(Blob photo){
        User user = getUserById()
    }*/

    @DeleteMapping("/users/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    private User setUpdatedUser(User user, User userDetails)
    {
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setTimeZoneId(userDetails.getTimeZoneId());
        user.setPersonalDetails(userDetails.getPersonalDetails());
        user.setAppearance(userDetails.getAppearance());

        return user;
    }

}
