package pl.poul12.matchzone.service;

import org.springframework.stereotype.Service;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Appearance;
import pl.poul12.matchzone.repository.AppearanceRepository;

import java.util.Optional;

@Service
public class AppearanceServiceImpl implements AppearanceService{

    private AppearanceRepository appearanceRepository;

    public AppearanceServiceImpl(AppearanceRepository appearanceRepository) {
        this.appearanceRepository = appearanceRepository;
    }

    public Appearance saveAppearance(Appearance appearance){
        return appearanceRepository.save(appearance);
    }

    public Appearance getAppearance(Long userId) {

        Optional<Appearance> appearanceFound = appearanceRepository.findByUserId(userId);

        return appearanceFound.orElseThrow(() -> new ResourceNotFoundException("Appearance not found for this id: " + userId));
    }

    public Appearance updateAppearance(Long userId, Appearance appearance) {

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
