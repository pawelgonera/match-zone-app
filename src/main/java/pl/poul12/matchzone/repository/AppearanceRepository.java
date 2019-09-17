package pl.poul12.matchzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poul12.matchzone.model.Appearance;

public interface AppearanceRepository extends JpaRepository<Appearance, Long> {
}
