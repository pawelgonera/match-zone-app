package pl.poul12.matchzone.service;

import pl.poul12.matchzone.model.Message;

import java.util.List;

public interface MessageService {

    Message getMessageById(Long id);

    List<Message> getAllByUser(String username);

    List<Message> getMessagesByAuthor(String author);

    Message createMessage(String username, Message message);

    Message editMessage(Long messageId, Message message);

    boolean deleteMessage(Long messageId);
}
