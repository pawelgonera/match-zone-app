package pl.poul12.matchzone.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.Appearance;
import pl.poul12.matchzone.model.PersonalDetails;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.repository.AppearanceRepository;
import pl.poul12.matchzone.repository.PersonalDetailsRepository;
import pl.poul12.matchzone.repository.UserRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PersonalDetailsRepository personalDetailsRepository;
    private AppearanceRepository appearanceRepository;

    public UserService(UserRepository userRepository, PersonalDetailsRepository personalDetailsRepository, AppearanceRepository appearanceRepository) {
        this.userRepository = userRepository;
        this.personalDetailsRepository = personalDetailsRepository;
        this.appearanceRepository = appearanceRepository;
    }

    public List<User> getAllUsers(){

        return userRepository.findAll();
    }

    public User createUser(User user){

        /*PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setUser(user);
        personalDetailsRepository.save(personalDetails);

        Appearance appearance = new Appearance();
        appearance.setUser(user);
        appearanceRepository.save(appearance);*/

        return userRepository.save(user);
    }

    public User getUserById(Long id) throws ResourceNotFoundException {

        Optional<User> userFound = userRepository.findById(id);

        return userFound.orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + id));
    }

    public ResponseEntity<String> savePhoto(Long id, MultipartFile file) throws ResourceNotFoundException {

        User user = getUserById(id);

        try {
            user.getPersonalDetails().setPhoto(file.getBytes());
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body("You successfully uploaded " + file.getOriginalFilename() + "!");
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Something went wrong with your file " + file.getOriginalFilename());
        }
    }

    public User updateUser(Long id, User user) throws ResourceNotFoundException {

        //User userFound = getUserById(id);
        //User userUpdated = setUserToUpdate(userFound, user);

        return userRepository.save(user);
    }

    public Map<String, Boolean> deleteUser(Long id) throws ResourceNotFoundException {

        User user = getUserById(id);

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


    private User setUserToUpdate(User userFound, User user){

        userFound.setUsername(user.getUsername());
        userFound.setEmail(user.getEmail());
        userFound.setPassword(user.getPassword());
        userFound.setTimeZoneId(user.getTimeZoneId());

        PersonalDetails personalDetails = user.getPersonalDetails();
        userFound.getPersonalDetails().setFirstName(personalDetails.getFirstName());
        userFound.getPersonalDetails().setCountry(personalDetails.getCountry());
        userFound.getPersonalDetails().setCity(personalDetails.getCity());
        userFound.getPersonalDetails().setDateOfBirth(personalDetails.getDateOfBirth());
        userFound.getPersonalDetails().setEducation(personalDetails.getEducation());
        userFound.getPersonalDetails().setMaritalStatus(personalDetails.getMaritalStatus());
        userFound.getPersonalDetails().setOccupation(personalDetails.getOccupation());
        userFound.getPersonalDetails().setReligion(personalDetails.getReligion());
        userFound.setPersonalDetails(personalDetails);

        Appearance appearance = user.getAppearance();
        userFound.getAppearance().setEyes(appearance.getEyes());
        userFound.getAppearance().setHairColour(appearance.getHairColour());
        userFound.getAppearance().setHeight(appearance.getHeight());
        userFound.getAppearance().setPhysique(appearance.getPhysique());
        userFound.getAppearance().setAbout(appearance.getAbout());
        userFound.getAppearance().setHobbies(appearance.getHobbies());
        userFound.setAppearance(appearance);

        return userFound;
    }


}
