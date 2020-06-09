package pl.poul12.matchzone.service;

import org.springframework.stereotype.Service;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.repository.PersonalDetailsRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PersonalDetailsServiceImpl implements PersonalDetailsService {

    private PersonalDetailsRepository personalDetailsRepository;

    public PersonalDetailsServiceImpl(PersonalDetailsRepository personalDetailsRepository) {
        this.personalDetailsRepository = personalDetailsRepository;
    }

    public PersonalDetails savePersonalDetails(PersonalDetails personalDetails){
        return personalDetailsRepository.save(personalDetails);
    }

    @Transactional
    public PersonalDetails getPersonalDetails(Long userId) {

        Optional<PersonalDetails> personalDetailsFound = personalDetailsRepository.findByUserId(userId);

        return personalDetailsFound.orElseThrow(() -> new ResourceNotFoundException("PersonalDetails not found for this id: " + userId)
        );
    }

    public PersonalDetails updatePersonalDetails(Long userId, PersonalDetails personalDetails) {

        PersonalDetails personalDetailsFound = personalDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("PersonalDetails not found for this id: " + userId));

        personalDetailsFound.setGender(personalDetails.getGender());
        personalDetailsFound.setAge(personalDetails.getAge());
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
}
