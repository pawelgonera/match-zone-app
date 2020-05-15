package pl.poul12.matchzone.service;

import org.springframework.stereotype.Service;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Message;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.repository.MessageRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private UserService userService;

    public MessageServiceImpl(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    @Override
    public Message getMessageById(Long id) {
        Optional<Message> messageFound = messageRepository.findById(id);
        return messageFound.orElseThrow(() -> new ResourceNotFoundException("Message not found for this id: " + id));
    }

    @Override
    public List<Message> getMessagesBySender(String sender) {
        List<Message> messages = messageRepository.findBySenderOrRecipient(sender, sender);
        if(messages.isEmpty())
        {
            throw new ResourceNotFoundException("Messages not found for this sender: " + sender);
        }else {
            return messages;
        }
    }

    @Override
    public List<Message> getMessagesByRecipient(String recipient, String sender) {
        List<Message> messages = messageRepository.findBySenderAndRecipientOrSenderAndRecipientOrderByPostDate(sender, recipient, recipient, sender);

        if(messages.isEmpty())
        {
            throw new ResourceNotFoundException("Messages not found for this recipient: " + recipient);
        }else {
            return messages;
        }
    }

    @Override
    public Set<User> getMembers(String owner) {

        List<Message> messages = getMessagesBySender(owner);

        Set<User> members = new HashSet<>();

        for(Message message : messages){
            User recipient = userService.getUserByUsername(message.getRecipient());
            members.add(recipient);
            User sender = userService.getUserByUsername(message.getSender());
            members.add(sender);
        }

        User user = userService.getUserByUsername(owner);
        members.remove(user);

        return members;
    }


    @Override
    public Message createMessage(String username, Message message) {
        User user = userService.getUserByUsername(username);
        message.setUser(user);

        return messageRepository.save(message);
    }

    @Override
    public Message editMessage(Long messageId, Message message) {
        Message messageFound = getMessageById(messageId);
        messageFound.setContent(message.getContent());
        messageFound.setPostDate(message.getPostDate());

        return messageRepository.save(messageFound);
    }

    @Override
    public boolean deleteMessage(Long messageId) {
        try{
            messageRepository.deleteById(messageId);
        }catch (Exception e){
            return false;
        }

        return true;
    }
}