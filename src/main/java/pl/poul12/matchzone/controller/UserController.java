package pl.poul12.matchzone.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.support.PagedListHolder;
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

import pl.poul12.matchzone.model.Comment;
import pl.poul12.matchzone.model.Message;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.forms.FilterForm;
import pl.poul12.matchzone.security.JwtProvider;
import pl.poul12.matchzone.security.JwtResponse;
import pl.poul12.matchzone.security.forms.ChangeEmailForm;
import pl.poul12.matchzone.security.forms.ChangePasswordForm;
import pl.poul12.matchzone.security.forms.LoginForm;
import pl.poul12.matchzone.security.forms.RegisterForm;
import pl.poul12.matchzone.service.*;
import pl.poul12.matchzone.util.CustomErrorResponse;
import pl.poul12.matchzone.util.FileValidator;
import pl.poul12.matchzone.util.MailSender;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@CrossOrigin(value = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api/v1")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private FileValidator validator = new FileValidator();

    private PersonalDetailsService personalDetailsService;
    private CommentService commentService;
    private MessageService messageService;

    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private UserService userService;
    private MailSender mailSender;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public UserController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserService userService,
                          MailSender mailSender, PersonalDetailsService personalDetailsService, CommentService commentService, MessageService messageService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.mailSender = mailSender;
        this.personalDetailsService = personalDetailsService;
        this.commentService = commentService;
        this.messageService = messageService;
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

        String error = errorsFormatter(errors);

        if(errors.hasErrors()){
            return new ResponseEntity<>(new CustomErrorResponse(error), HttpStatus.BAD_REQUEST);
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

    @PostMapping("users/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordForm userDetails, Errors errors){
        String error = errorsFormatter(errors);

        if(errors.hasErrors()){
            return new ResponseEntity<>(new CustomErrorResponse(error), HttpStatus.BAD_REQUEST);
        }else{
            User user = userService.getUserByUsername(userDetails.getUsername());
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).build();
        }

    }

    @PostMapping("users/change-email")
    public ResponseEntity<?> changePEmail(@Valid @RequestBody ChangeEmailForm userDetails, Errors errors){
        String error = errorsFormatter(errors);

        if(errors.hasErrors()){
            return new ResponseEntity<>(new CustomErrorResponse(error), HttpStatus.BAD_REQUEST);
        }else{
            User user = userService.getUserByUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).build();
        }

    }

    @PostMapping("/users/{username}/change-avatar")
    public ResponseEntity<?> changeAvatar(@PathVariable(value = "username") String username, @RequestParam("file")MultipartFile file) {

        User user = userService.getUserByUsername(username);
        System.out.println("before personaldetails");
        PersonalDetails personalDetails = personalDetailsService.getPersonalDetails(user.getId());
        System.out.println("before get comment in controller");
        List<Comment> comments = commentService.getCommentsByAuthor(username);
        System.out.println("Comments: " + comments);
        List<Message> messages =  messageService.getMessagesBySender(username);
        System.out.println("Messages: " + messages);

        try {
            try {
                return validator.validate(file);
            }catch (RuntimeException e){

                //userService.changeAvatar(username, file);
                personalDetails.setPhoto(file.getBytes());

                if(!comments.isEmpty()) {
                    for (Comment comment : comments) {
                        comment.setAvatar(file.getBytes());
                    }
                }

                for(Message message : messages){
                    message.setAvatar(file.getBytes());
                }

                personalDetailsService.savePersonalDetails(personalDetails);
                logger.info("Photo uploaded");
            }
            return ResponseEntity.status(HttpStatus.OK).body("You successfully uploaded " + file.getOriginalFilename() + "!");
        }catch (IOException e){
            logger.error("Something went wrong with your file: {}", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Something went wrong with your file" + file.getOriginalFilename());
        }

    }

    @PutMapping("/users/{username}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "username") String username, @Valid @RequestBody User userDetails, Errors errors) {

        String error = errorsFormatter(errors);

        if(errors.hasErrors()){
            return new ResponseEntity<>(new CustomErrorResponse(error), HttpStatus.BAD_REQUEST);
        }else{
            final User updatedUser = userService.updateUser(username, userDetails);
            return ResponseEntity.ok(updatedUser);
        }
    }

    @DeleteMapping("/users/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) {

        return userService.deleteUser(userId);
    }

    @PostMapping("/users/filter")
    public PagedListHolder<User> getFilteredUserList(@Valid @RequestBody FilterForm filterForm){
        return userService.filterUserList(filterForm);
    }

    private String errorsFormatter(Errors errors){
        StringBuilder sb = new StringBuilder();
        errors.getAllErrors().forEach(error -> sb.append(error.getDefaultMessage()).append("-"));

        String error;
        if(sb.toString().isEmpty()){
            error = sb.toString();
        }else {
            error = sb.toString().substring(0, sb.length()-1);
        }

        System.out.println("error: " + error);

        return error;
    }
}
