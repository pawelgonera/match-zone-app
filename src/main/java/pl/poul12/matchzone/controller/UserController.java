package pl.poul12.matchzone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.repository.UserRepository;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class UserController {

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

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetails) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));

        final User updatedUser = userRepository.save(setUpdatedUser(user, userDetails));
        return ResponseEntity.ok(updatedUser);
    }

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
