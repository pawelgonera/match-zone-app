package pl.poul12.matchzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poul12.matchzone.model.Appearance;

import java.util.Optional;

public interface AppearanceRepository extends JpaRepository<Appearance, Long> {

    Optional<Appearance> findByUserId(Long userId);
}
