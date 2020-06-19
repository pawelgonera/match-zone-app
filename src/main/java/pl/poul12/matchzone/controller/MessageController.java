package pl.poul12.matchzone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.poul12.matchzone.model.Message;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.service.MessageService;
import pl.poul12.matchzone.service.PersonalDetailsServiceImpl;
import pl.poul12.matchzone.service.UserService;
import pl.poul12.matchzone.util.CustomErrorResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api/v1")
public class MessageController {

    private MessageService messageService;
    private PersonalDetailsServiceImpl personalDetailsService;
    private UserService userService;

    public MessageController(MessageService messageService, PersonalDetailsServiceImpl personalDetailsService, UserService userService) {
        this.messageService = messageService;
        this.personalDetailsService = personalDetailsService;
        this.userService = userService;
    }

    @GetMapping("/messages/{recipient}/{sender}")
    public ResponseEntity<List<Message>> getAllByRecipient(@PathVariable(value = "recipient") String recipient, @PathVariable(value = "sender") String sender){

        List<Message> messages = messageService.getMessagesByRecipient(recipient, sender);
        return ResponseEntity.ok().body(messages);
    }

    @GetMapping("/messages/{sender}")
    public ResponseEntity<List<Message>> getAllBySender(@PathVariable(value = "sender") String sender){

        List<Message> messages = messageService.getAllMessagesToAllBySender(sender);
        return ResponseEntity.ok().body(messages);
    }

    @GetMapping("/messages/last-message/{sender}")
    public ResponseEntity<Message> getLastMessageToAllBySender(@PathVariable(value = "sender") String sender){

        Message message = messageService.getLastMessageToAllBySender(sender);
        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/messages/members/{sender}")
    public ResponseEntity<Set<User>> getAllMembers(@PathVariable(value = "sender") String sender){

        Set<User> members = messageService.getMembers(sender);
        return ResponseEntity.ok().body(members);
    }

    @PostMapping("/messages/is-new-message-from-recipient/")
    public ResponseEntity<Boolean> isNewMessageFromRecipient(@RequestBody Message message){

        System.out.println("Message(byRecipient): " + message);

        boolean isNew = messageService.isNewMessageFromRecipient(message);

        return ResponseEntity.ok(isNew);
    }

    @PostMapping("/messages/is-new-message-from-sender/{username}")
    public ResponseEntity<Boolean> isNewMessageFromSender(@RequestBody Message message, @PathVariable(value = "username") String username){

        System.out.println("Message(bySender): " + message + " : " + username );

        boolean isNew = messageService.isNewMessageFromSender(message, username);

        return ResponseEntity.ok(isNew);
    }

    @PostMapping("/messages/{username}")
    public ResponseEntity<?> addMessage(@PathVariable(value = "username") String username, @RequestBody Message message) {

        Long userId = userService.getUserByUsername(username).getId();
        PersonalDetails personalDetails = personalDetailsService.getPersonalDetails(userId);
        message.setAvatar(personalDetails.getPhoto());

        messageService.createMessage(username, message);

        return new ResponseEntity<>(new CustomErrorResponse("Message created successfully"), HttpStatus.OK);
    }

    @PutMapping("/messages/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable(value = "id") Long messageId, @Valid @RequestBody Message message) {

        final Message updatedMessage = messageService.editMessage(messageId, message);

        return ResponseEntity.ok(updatedMessage);
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> removeMessage(@PathVariable(value = "id") Long messageId){

        boolean isRemoved = messageService.deleteMessage(messageId);

        if(isRemoved){
            return new ResponseEntity<>(new CustomErrorResponse("Message removed successfully"), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new CustomErrorResponse("Message cannot be removed"), HttpStatus.BAD_REQUEST);
        }
    }
}
