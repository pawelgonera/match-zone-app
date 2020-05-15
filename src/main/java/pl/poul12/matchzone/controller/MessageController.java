package pl.poul12.matchzone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.poul12.matchzone.model.Message;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.service.MessageService;
import pl.poul12.matchzone.service.PersonalDetailsServiceImpl;
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

    public MessageController(MessageService messageService, PersonalDetailsServiceImpl personalDetailsService) {
        this.messageService = messageService;
        this.personalDetailsService = personalDetailsService;
    }

    @GetMapping("/messages/{recipient}/{sender}")
    public ResponseEntity<List<Message>> getAllByRecipient(@PathVariable(value = "recipient") String recipient, @PathVariable(value = "sender") String sender){

        List<Message> messages = messageService.getMessagesByRecipient(recipient, sender);
        return ResponseEntity.ok().body(messages);

    }

    @GetMapping("/messages/members/{sender}")
    public ResponseEntity<Set<User>> getAllMembers(@PathVariable(value = "sender") String sender){

        Set<User> members = messageService.getMembers(sender);
        return ResponseEntity.ok().body(members);
    }

    @PostMapping("/messages/{username}")
    public ResponseEntity<?> addMessage(@PathVariable(value = "username") String username, @RequestBody Message message) {

        PersonalDetails personalDetails = personalDetailsService.getPersonalDetails(message.getSender());
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