package pl.poul12.matchzone.service;

import org.springframework.stereotype.Service;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.repository.PersonalDetailsRepository;
import pl.poul12.matchzone.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PersonalDetailsService {

    private PersonalDetailsRepository personalDetailsRepository;
    private UserService userService;

    public PersonalDetailsService(PersonalDetailsRepository personalDetailsRepository, UserService userService) {
        this.personalDetailsRepository = personalDetailsRepository;
        this.userService = userService;
    }

    @Transactional
    public PersonalDetails getPersonalDetails(String username) throws ResourceNotFoundException {

        Long userId = getUserId(username);

        Optional<PersonalDetails> personalDetailsFound = personalDetailsRepository.findByUserId(userId);

        return personalDetailsFound.orElseThrow(() -> new ResourceNotFoundException("PersonalDetails not found for this id: " + userId)
        );
    }

    public PersonalDetails updatePersonalDetails(String username, PersonalDetails personalDetails) throws ResourceNotFoundException {

        Long userId = getUserId(username);

        PersonalDetails personalDetailsFound = personalDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("PersonalDetails not found for this id: " + userId));

        personalDetailsFound.setGender(personalDetails.getGender());
        personalDetailsFound.setReligion(personalDetails.getReligion());
        personalDetailsFound.setOccupation(personalDetails.getOccupation());
        personalDetailsFound.setMaritalStatus(personalDetails.getMaritalStatus());
        personalDetailsFound.setEducation(personalDetails.getEducation());
        personalDetailsFound.setCity(personalDetails.getCity());
        personalDetailsFound.setCountry(personalDetails.getCountry());
        personalDetailsFound.setDateOfBirth(personalDetails.getDateOfBirth());
        personalDetailsFound.setRating(personalDetails.getRating());

        return personalDetailsRepository.save(personalDetailsFound);
    }

    private Long getUserId(String username) throws ResourceNotFoundException {
        User user = userService.getUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Appearance not found for this username: " + username));
        return user.getId();
    }
}
