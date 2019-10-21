package pl.poul12.matchzone.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.poul12.matchzone.exception.ResourceNotFoundException;

import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.forms.FilterForm;
import pl.poul12.matchzone.security.JwtProvider;
import pl.poul12.matchzone.security.JwtResponse;
import pl.poul12.matchzone.security.forms.LoginForm;
import pl.poul12.matchzone.security.forms.RegisterForm;
import pl.poul12.matchzone.service.UserService;
import pl.poul12.matchzone.util.CustomErrorResponse;


import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api/v1")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private UserService userService;

    public UserController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUser(@PathVariable(value = "username") String username) {

        User user = userService.getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterForm register, Errors errors) {

        StringBuilder sb = new StringBuilder();
        errors.getAllErrors().forEach(error -> sb.append(error.getDefaultMessage())
                                                 .append("-"));

        sb.delete(sb.length()-1, sb.length());

        if(errors.hasErrors()){
            return new ResponseEntity<>(new CustomErrorResponse(sb.toString()), HttpStatus.BAD_REQUEST);
        }

        userService.createUser(register);

        return new ResponseEntity<>(new CustomErrorResponse("User registered successfully!"), HttpStatus.OK);
    }


    @PostMapping("/users/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginForm login) {

        logger.info("Start of login controller");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        logger.info("login: username: {} password: {}", login.getUsername(), login.getPassword());
        logger.info("login controller after authentication");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("login controller after securitycontextholder");
        String jwt = jwtProvider.generateJwtToken(authentication);
        logger.info("login controller after generateJwtToken");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        logger.info("userdetails username form login controller: " + userDetails.getUsername());
        logger.info("End of login controller");

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/users/{username}/change-avatar")
    public ResponseEntity<String> changeAvatar(@PathVariable(value = "username") String username, @RequestParam("file")MultipartFile file) throws ResourceNotFoundException {

        return userService.savePhoto(username, file);
    }

    @PutMapping("/users/{username}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "username") String username, @Valid @RequestBody User userDetails) throws ResourceNotFoundException {

        final User updatedUser = userService.updateUser(username, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {

        return userService.deleteUser(userId);
    }

    @PostMapping("/users/filter")
    public PagedListHolder<User> getFilteredUserList(@Valid @RequestBody FilterForm filterForm){

        return userService.filterUserList(filterForm);
    }

    @GetMapping("/users/list")
    public Page<User> getUsersPage(@RequestParam("page") int page, @RequestParam("size") int size){

        Sort sort = new Sort(Sort.Direction.ASC, "firstName");
        Pageable pageable = PageRequest.of(page, size, sort);
        return userService.getPageableListOfUsers(pageable);
    }
}
