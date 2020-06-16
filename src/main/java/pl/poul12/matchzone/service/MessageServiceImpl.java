package pl.poul12.matchzone.service;

import org.springframework.stereotype.Service;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Message;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.repository.MessageRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        List<Message> messages = messageRepository.findBySender(sender);
        if(messages.isEmpty())
        {
            throw new ResourceNotFoundException("Messages not found for this sender: " + sender);
        }else {
            return messages;
        }
    }

    @Override
    public List<Message> getAllMessagesToAllBySender(String sender){
        List<Message> messages = messageRepository.findAllBySenderOrRecipientOrderByPostDate(sender, sender);
        if(messages.isEmpty())
        {
            throw new ResourceNotFoundException("Messages not found for this sender: " + sender);
        }else {
            return messages;
        }
    }

    @Override
    public List<Message> getMessagesBySenderOrRecipient(String sender) {
        List<Message> messages = messageRepository.findBySenderOrRecipient(sender, sender);
        if(messages.isEmpty())
        {
            throw new ResourceNotFoundException("Messages not found for this sender or recipient: " + sender);
        }else {
            return messages;
        }
    }

    @Override
    public List<Message> getMessagesByRecipient(String recipient, String sender) {
        List<Message> messages = messageRepository.findBySenderAndRecipientOrSenderAndRecipientOrderByPostDate(sender, recipient, recipient, sender);

        if(messages.isEmpty())
        {
            return Collections.emptyList();
        }else {
            return messages;
        }
    }

    @Override
    public Set<User> getMembers(String owner) {

        List<Message> messages = getMessagesBySenderOrRecipient(owner);

        //System.out.println("messages: " + messages);

        Set<User> members = new HashSet<>();

        for(Message message : messages){
            User recipient = userService.getUserByUsername(message.getRecipient());
            members.add(recipient);
            User sender = userService.getUserByUsername(message.getSender());
            members.add(sender);
        }

        //System.out.println("members before: " + members);

        User user = userService.getUserByUsername(owner);
        members.remove(user);

        //System.out.println("members after: " + members);

        return members;
    }

    @Override
    public Message getLastMessageToRecipientBySender(String recipient, String sender) {

        List<Message> messages = getMessagesByRecipient(recipient, sender);
        return messages.get(messages.size() - 1);
        //return messageRepository.findFirstByRecipientAndSenderOrderByPostDateDesc(recipient, sender).orElseThrow(() -> new ResourceNotFoundException("Messages not found for this recipient and sender: " + recipient + " " + sender ));
    }

    @Override
    public Message getLastMessageToAllBySender(String sender) {

        return messageRepository.findFirstBySenderOrRecipientOrderByPostDateDesc(sender, sender).orElseThrow(() -> new ResourceNotFoundException("Messages not found for this sender or recipient: " + sender + " " + sender ));
    }

    @Override
    public boolean isNewMessageFromRecipient(Message lastMessage) {

        LocalDateTime lastMessagePostDateFromRecipient = getLastMessageToRecipientBySender(lastMessage.getRecipient(), lastMessage.getSender()).getPostDate();

        System.out.println("lastMessage: " + lastMessage.getPostDate());
        System.out.println("lastMessagePostDateFromRecipient: " + lastMessagePostDateFromRecipient);

        return lastMessage.getPostDate().isBefore(lastMessagePostDateFromRecipient);
    }

    @Override
    public boolean isNewMessageFromSender(Message lastMessage, String username) {

        LocalDateTime lastMessagePostDateFromSender = getLastMessageToAllBySender(username).getPostDate();

        System.out.println("lastMessage: " + lastMessage.getPostDate());
        System.out.println("lastMessagePostDateFromSender: " + lastMessagePostDateFromSender);

        return lastMessage.getPostDate().isBefore(lastMessagePostDateFromSender);
    }

    @Override
    public Message createMessage(String username, Message message) {
        User user = userService.getUserByUsername(username);
        message.setUser(user);

        /*ZoneId zoneId = ZoneId.of(user.getTimeZoneId());
        System.out.println("zoneId: " + zoneId);
        ZonedDateTime zonedDateTimeInUTC = LocalDateTime.parse("2020-05-20T21:18:33.043").atZone(ZoneId.of("UTC"));
        System.out.println("postDate: " + message.getPostDate());
        ZonedDateTime zonedDateTimeInCustomZone = zonedDateTimeInUTC.withZoneSameInstant(ZoneId.of(user.getTimeZoneId()));
        System.out.println("zonedDateTimeInCustomZone: " + zonedDateTimeInCustomZone);
        LocalDateTime postDateWithTimeZone = zonedDateTimeInCustomZone.toLocalDateTime();
        System.out.println("postDateWithTimeZone: " + postDateWithTimeZone);
        message.setPostDate(postDateWithTimeZone);*/

        message.setPostDate(message.getPostDate().plusHours(2));

        return messageRepository.save(message);
    }

    @Override
    public Message editMessage(Long messageId, Message message) {
        Message messageFound = getMessageById(messageId);
        //messageFound.setContent(message.getContent());
        //messageFound.setPostDate(message.getPostDate());
        messageFound.setUnread(message.isUnread());

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