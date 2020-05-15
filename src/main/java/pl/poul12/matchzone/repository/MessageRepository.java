package pl.poul12.matchzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.poul12.matchzone.model.Message;
import pl.poul12.matchzone.model.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByUser(User user);

    List<Message> findBySenderOrRecipient(String sender, String recipient);

    List<Message> findBySenderAndRecipientOrSenderAndRecipientOrderByPostDate(String sender1, String recipient1, String sender2, String recipient2);

}