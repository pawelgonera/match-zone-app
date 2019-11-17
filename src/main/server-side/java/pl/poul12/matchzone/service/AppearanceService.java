package pl.poul12.matchzone.service;

import org.springframework.stereotype.Service;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Appearance;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.repository.AppearanceRepository;

import java.util.Optional;

@Service
public class AppearanceService {

    private AppearanceRepository appearanceRepository;
    private UserServiceImpl userServiceImpl;

    public AppearanceService(AppearanceRepository appearanceRepository, UserServiceImpl userServiceImpl) {
        this.appearanceRepository = appearanceRepository;
        this.userServiceImpl = userServiceImpl;
    }

    public Appearance getAppearance(String username) {

        Long userId = getUserId(username);

        Optional<Appearance> appearanceFound = appearanceRepository.findByUserId(userId);

        return appearanceFound.orElseThrow(() -> new ResourceNotFoundException("Appearance not found for this id: " + userId)
        );
    }

    public Appearance updateAppearance(String username, Appearance appearance) {

        Long userId = getUserId(username);

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

    private Long getUserId(String username) {
        User user = userServiceImpl.getUserByUsername(username);
        return user.getId();
    }
}
