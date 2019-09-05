package pl.poul12.matchzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poul12.matchzone.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
