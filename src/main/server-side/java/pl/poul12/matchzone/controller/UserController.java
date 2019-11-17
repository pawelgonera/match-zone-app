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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.poul12.matchzone.exception.ResourceNotFoundException;

import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.forms.FilterForm;
import pl.poul12.matchzone.security.JwtProvider;
import pl.poul12.matchzone.security.JwtResponse;
import pl.poul12.matchzone.security.forms.LoginForm;
import pl.poul12.matchzone.security.forms.RegisterForm;
import pl.poul12.matchzone.service.PersonalDetailsService;
import pl.poul12.matchzone.service.UserService;
import pl.poul12.matchzone.service.UserServiceImpl;
import pl.poul12.matchzone.util.CustomErrorResponse;
import pl.poul12.matchzone.util.MailSender;


import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api/v1")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final static Long MAX_FILE_SIZE = 10_000_000L;
    private static String IS_IMAGE_TYPE = Pattern.compile("image/.+").pattern();

    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private UserService userService;
    private PersonalDetailsService personalDetailsService;
    private MailSender mailSender;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public UserController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserService userService,
                          MailSender mailSender, PersonalDetailsService personalDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.mailSender = mailSender;
        this.personalDetailsService = personalDetailsService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUser(@PathVariable(value = "username") String username) {

        User user = userService.getUserByUsername(username);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterForm register, Errors errors) {

        StringBuilder sb = new StringBuilder();
        errors.getAllErrors().forEach(error -> sb.append(error.getDefaultMessage())
                                                 .append("-"));

        //sb.delete(sb.length()-1, sb.length());

        if(errors.hasErrors()){
            return new ResponseEntity<>(new CustomErrorResponse(sb.toString()), HttpStatus.BAD_REQUEST);
        }else{
            userService.createUser(register);
            return new ResponseEntity<>(new CustomErrorResponse("User registered successfully!"), HttpStatus.OK);
        }
    }


    @PostMapping("/users/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginForm login) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("users/reset-pass")
    public ResponseEntity<?> resetPassword(@RequestParam String email){

        String hashedText = passwordEncoder.encode(email);
        String newPassword = hashedText.substring(hashedText.length() - 12);

        User user = userService.getUserByEmail(email);

        try {
            mailSender.sendEmail(email, newPassword);
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("Password has been changed successfully");

        }catch (MessagingException e){
            e.printStackTrace();
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/users/{username}/change-avatar")
    public ResponseEntity<?> changeAvatar(@PathVariable(value = "username") String username, @RequestParam("file")MultipartFile file) {

        PersonalDetails personalDetails = personalDetailsService.getPersonalDetails(username);

        try {

            System.out.println(file.getSize());
            System.out.println(file.getContentType());
            if(file.getSize() > MAX_FILE_SIZE)
            {
                return new ResponseEntity<>(new CustomErrorResponse("File size is too large, maximum size is 10 MB"), HttpStatus.BAD_REQUEST);
            }

            if(!Objects.requireNonNull(file.getContentType()).matches(IS_IMAGE_TYPE)){
                return new ResponseEntity<>(new CustomErrorResponse("Media type not required, it must be an image type"), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }

            personalDetails.setPhoto(file.getBytes());
            personalDetailsService.savePersonalDetails(personalDetails);
            logger.info("Photo uploaded");
            return ResponseEntity.status(HttpStatus.OK).body("You successfully uploaded " + file.getOriginalFilename() + "!");
        }catch (IOException e){
            logger.error("Something went wrong with your file: {}", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Something went wrong with your file" + file.getOriginalFilename());
        }

    }

    @PutMapping("/users/{username}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "username") String username, @Valid @RequestBody User userDetails) {

        final User updatedUser = userService.updateUser(username, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) {

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
