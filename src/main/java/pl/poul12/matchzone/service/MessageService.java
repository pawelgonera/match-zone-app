package pl.poul12.matchzone.service;

import pl.poul12.matchzone.model.Message;
import pl.poul12.matchzone.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface MessageService {

    Message getMessageById(Long id);

    List<Message> getMessagesBySender(String sender);

    List<Message> getAllMessagesToAllBySender(String sender);

    List<Message> getMessagesBySenderOrRecipient(String sender);

    List<Message> getMessagesByRecipient(String recipient, String sender);

    Set<User> getMembers(String owner);

    Message getLastMessageToAllBySender(String sender);

    Message getLastMessageToRecipientBySender(String recipient, String sender);

    boolean isNewMessageFromRecipient(Message lastMessage);

    boolean isNewMessageFromSender(Message lastMessage, String username);

    Message createMessage(String username, Message message);

    Message editMessage(Long messageId, Message message);

    boolean deleteMessage(Long messageId);
}