package pl.poul12.matchzone.service;

import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Comment;
import pl.poul12.matchzone.model.Message;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

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
    public List<Message> getAllByUser(String username) {
        User user = userService.getUserByUsername(username);
        return messageRepository.findAllByUser(user);
    }

    @Override
    public List<Message> getMessagesByAuthor(String author) {
        List<Message> messages = messageRepository.findByAuthor(author);
        if(messages.isEmpty())
        {
            throw new ResourceNotFoundException("Messages not found for this id: " + author);
        }else {
            return messages;
        }
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
