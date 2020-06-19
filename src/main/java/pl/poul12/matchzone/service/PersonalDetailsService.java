package pl.poul12.matchzone.service;

import pl.poul12.matchzone.model.PersonalDetails;

public interface PersonalDetailsService {
    PersonalDetails savePersonalDetails(PersonalDetails personalDetails);

    PersonalDetails getPersonalDetails(Long userId);

    PersonalDetails updatePersonalDetails(Long userId, PersonalDetails personalDetails);
}
