package pl.poul12.matchzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poul12.matchzone.model.MailData;


public interface MailDataRepository extends JpaRepository<MailData, Long>
{

}
