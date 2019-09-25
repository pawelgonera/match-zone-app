package pl.poul12.matchzone.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.poul12.matchzone.exception.ResourceNotFoundException;

import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.service.UserService;
import pl.poul12.matchzone.util.CustomErrorResponse;


import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/v1")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @GetMapping("/users")
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }

    @CrossOrigin
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {

        User user = userService.getUserById(userId);

        return ResponseEntity.ok().body(user);
    }

    @CrossOrigin
    @PostMapping("/users/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {

        Optional<User> userFound = userService.getUserByUsername(user.getUsername());

        if(userFound.isPresent()){
            logger.error("User with username: {} already exist", user.getUsername());
            return new ResponseEntity<>(new CustomErrorResponse("User with username " + user.getUsername() + "already exist "), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping("/users/login")
    public Principal user(Principal principal) {
        logger.info("User logged - {}", principal);
        System.out.println("principal: " + principal.toString());
        System.out.println("prncipalName: " + principal.getName());

        return principal;
    }

    @PostMapping("/users/{id}/change-avatar")
    public ResponseEntity<String> changeAvatar(@PathVariable(value = "id") Long userId, @RequestParam("file")MultipartFile file) throws ResourceNotFoundException {

        return userService.savePhoto(userId, file);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetails) throws ResourceNotFoundException {

        final User updatedUser = userService.updateUser(userId, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {

        return userService.deleteUser(userId);
    }


}
