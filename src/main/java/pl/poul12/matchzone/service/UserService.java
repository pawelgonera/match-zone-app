package pl.poul12.matchzone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.beans.support.SortDefinition;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.*;
import pl.poul12.matchzone.model.enums.RoleName;
import pl.poul12.matchzone.model.forms.FilterForm;
import pl.poul12.matchzone.repository.*;
import pl.poul12.matchzone.security.forms.RegisterForm;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private PagingUserRepository pagingUserRepository;

    private PersonalDetailsRepository personalDetailsRepository;
    private AppearanceRepository appearanceRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PersonalDetailsRepository personalDetailsRepository,
                       AppearanceRepository appearanceRepository, PagingUserRepository pagingUserRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.personalDetailsRepository = personalDetailsRepository;
        this.appearanceRepository = appearanceRepository;
        this.pagingUserRepository = pagingUserRepository;
    }

    public List<User> getAllUsers(){

        return userRepository.findAll();
    }

    public List<User> getAllUsersBySort(Sort sort){

        return userRepository.findAll(sort);
    }

    public Page<User> getPageableListOfUsers(Pageable pageable){
        Page<User> page = userRepository.findAll(pageable);
        System.out.println("From ordinary page: content - " + page.getContent().size());
        return userRepository.findAll(pageable);
    }

    public User createUser(RegisterForm registerUser){

        User user = buildUser(registerUser);

        Set<String> strRoles = registerUser.getRole();

        Set<Role> roles = new HashSet<>();
        for(String role : strRoles) {
            Role roleFound = roleRepository.findByName(RoleName.valueOf(role.toUpperCase())).orElseThrow(() -> new RuntimeException("Not found any role!"));
            roles.add(roleFound);
        }

        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User getUserById(Long id) throws ResourceNotFoundException {

        Optional<User> userFound = userRepository.findById(id);

        return userFound.orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + id)
        );
    }

    public Optional<User> getUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findUserByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) throws UsernameNotFoundException {

        return userRepository.findUserByEmail(email);
    }

    public ResponseEntity<String> savePhoto(String username, MultipartFile file) throws ResourceNotFoundException {

        User user = getUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found for this username: " + username));
        PersonalDetails personalDetails = personalDetailsRepository.findByUserId(user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("PersonalDetails not found for this id: " + user.getId())
        );

        try {
            personalDetails.setPhoto(file.getBytes());
            personalDetailsRepository.save(personalDetails);
            logger.info("Photo uploaded");
            return ResponseEntity.status(HttpStatus.OK).body("You successfully uploaded " + file.getOriginalFilename() + "!");
        }catch (IOException e){
            logger.error("Something went wrong with your file: {}", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Something went wrong with your file" + file.getOriginalFilename());
        }
    }

    public User updateUser(String username, User user) throws ResourceNotFoundException {

        User userFound = getUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("PersonalDetails not found for this username: " + username));

        User userUpdated = updateUser(userFound, user);

        return userRepository.save(userUpdated);
    }

    public Map<String, Boolean> deleteUser(Long id) throws ResourceNotFoundException {

        User user = getUserById(id);

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    public PagedListHolder<User> filterUserList(FilterForm filterForm) {

        boolean isNameIsEmpty = filterForm.getName().isEmpty();
        boolean isGenderIsUndefined = filterForm.getGender().ordinal() == 0;
        boolean isAgeIsZero = filterForm.getAgeMin() == 0 && filterForm.getAgeMax() == 0;
        boolean isCityIsEmpty = filterForm.getCity().isEmpty();
        boolean isRatingIsZero = filterForm.getRatingMin() == 0 && filterForm.getRatingMax() == 0;

        Sort sort = Sort.by(Sort.Direction.fromString(filterForm.getPageUser().getDirection()), filterForm.getPageUser().getSort());

        List<User> users = getAllUsersBySort(sort);

        if (!isNameIsEmpty) {
            users = userRepository.findAllByFirstNameStartingWithIgnoreCase(filterForm.getName(), sort);
        }

        if (!isGenderIsUndefined) {
            if (isNameIsEmpty) {
                users = userRepository.findAllByPersonalDetails_Gender(filterForm.getGender(), sort);
            } else {
                users = users.stream()
                        .filter(user -> user.getPersonalDetails().getGender() == filterForm.getGender())
                        .collect(Collectors.toList());
            }
        }

        if (!isAgeIsZero) {
            if (filterForm.getAgeMax() == 0) {
                filterForm.setAgeMax(filterForm.getAgeMin());
            }

            if (isNameIsEmpty && isGenderIsUndefined) {
                users = userRepository.findAllByPersonalDetails_AgeBetween(filterForm.getAgeMin(), filterForm.getAgeMax(), sort);
            }else {
                users = users.stream()
                        .filter(user -> user.getPersonalDetails().getAge() >= filterForm.getAgeMin() && user.getPersonalDetails().getAge() <= filterForm.getAgeMax())
                        .collect(Collectors.toList());
            }
        }

        if (!isRatingIsZero) {
            if (isNameIsEmpty && isGenderIsUndefined && isAgeIsZero) {
                users = userRepository.findAllByPersonalDetails_RatingBetween(filterForm.getRatingMin(), filterForm.getRatingMax(), sort);
            }else {
                users = users.stream()
                        .filter(user -> user.getPersonalDetails().getRating() >= filterForm.getRatingMin() && user.getPersonalDetails().getRating() <= filterForm.getRatingMax())
                        .collect(Collectors.toList());
            }
        }

        if (!isCityIsEmpty) {
            if(isNameIsEmpty && isGenderIsUndefined && isAgeIsZero && isRatingIsZero) {
                users = userRepository.findAllByPersonalDetails_City(filterForm.getCity(), sort);
            }else {
                users = users.stream()
                        .filter(user -> user.getPersonalDetails().getCity().equals(filterForm.getCity()))
                        .collect(Collectors.toList());
            }
        }

        PagedListHolder<User> pagedListHolder = new PagedListHolder<>(users);
        pagedListHolder.setPage(filterForm.getPageUser().getPage());
        pagedListHolder.setPageSize(filterForm.getPageUser().getSize());

        return pagedListHolder;
    }

    private User buildUser(RegisterForm registerUser){

        User user = new User();
        user.setUsername(registerUser.getUsername());
        user.setFirstName(registerUser.getName());
        user.setEmail(registerUser.getEmail());
        user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setDateOfBirth(registerUser.getDateOfBirth());
        //personalDetails.setDateOfBirth(LocalDate.parse(registerUser.getDateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        personalDetails.setAge(registerUser.getAge());
        personalDetails.setGender(personalDetails.getGender());
        personalDetails.setUser(user);
        Appearance appearance = new Appearance();
        appearance.setUser(user);

        personalDetailsRepository.save(personalDetails);
        appearanceRepository.save(appearance);

        return user;
    }

    private User updateUser(User userFound, User user){

        userFound.setUsername(user.getUsername());
        userFound.setFirstName(user.getFirstName());
        userFound.setPassword(passwordEncoder.encode(user.getPassword()));
        userFound.setEmail(user.getEmail());
        userFound.setTimeZoneId(user.getTimeZoneId());

        return userFound;
    }


}
