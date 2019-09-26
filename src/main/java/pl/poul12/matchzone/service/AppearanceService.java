package pl.poul12.matchzone.service;

import org.springframework.stereotype.Service;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Appearance;
import pl.poul12.matchzone.repository.AppearanceRepository;

import java.util.Optional;

@Service
public class AppearanceService {

    private AppearanceRepository appearanceRepository;

    public AppearanceService(AppearanceRepository appearanceRepository) {
        this.appearanceRepository = appearanceRepository;
    }

    public Appearance getAppearanceById(Long id) throws ResourceNotFoundException {

        Optional<Appearance> appearanceFound = appearanceRepository.findByUserId(id);

        return appearanceFound.orElseThrow(() -> new ResourceNotFoundException("Appearance not found for this id: " + id)
        );
    }

    public Appearance updateAppearance(Long userId, Appearance appearance) throws ResourceNotFoundException {

        Appearance appearanceFound = appearanceRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Appearance not found for this id: " + userId));

        appearanceFound.setHobbies(appearance.getHobbies());
        appearanceFound.setAbout(appearance.getAbout());
        appearanceFound.setPhysique(appearance.getPhysique());
        appearanceFound.setHeight(appearance.getHeight());
        appearanceFound.setHairColour(appearance.getHairColour());
        appearanceFound.setEyes(appearance.getEyes());

        return appearanceRepository.save(appearanceFound);
    }
}
