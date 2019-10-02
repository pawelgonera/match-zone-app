package pl.poul12.matchzone.service;

import org.springframework.stereotype.Service;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.repository.PersonalDetailsRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PersonalDetailsService {

    private PersonalDetailsRepository personalDetailsRepository;

    public PersonalDetailsService(PersonalDetailsRepository personalDetailsRepository) {
        this.personalDetailsRepository = personalDetailsRepository;
    }

    @Transactional
    public PersonalDetails getPersonalDetailsById(Long id) throws ResourceNotFoundException {

        Optional<PersonalDetails> personalDetailsFound = personalDetailsRepository.findByUserId(id);

        return personalDetailsFound.orElseThrow(() -> new ResourceNotFoundException("PersonalDetails not found for this id: " + id)
        );
    }

    public PersonalDetails updatePersonalDetails(Long userId, PersonalDetails personalDetails) throws ResourceNotFoundException {

        PersonalDetails personalDetailsFound = personalDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("PersonalDetails not found for this id: " + userId));

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
