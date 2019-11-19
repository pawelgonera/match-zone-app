package pl.poul12.matchzone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.model.*;
import pl.poul12.matchzone.model.enums.RoleName;
import pl.poul12.matchzone.model.forms.FilterForm;
import pl.poul12.matchzone.repository.*;
import pl.poul12.matchzone.security.forms.RegisterForm;
import pl.poul12.matchzone.service.filter.*;

import java.util.*;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private PersonalDetailsRepository personalDetailsRepository;
    private AppearanceRepository appearanceRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PersonalDetailsRepository personalDetailsRepository,
                           AppearanceRepository appearanceRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.personalDetailsRepository = personalDetailsRepository;
        this.appearanceRepository = appearanceRepository;
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

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User createUser(RegisterForm registerUser){

        User user = buildUser(registerUser);

        Set<String> strRoles = registerUser.getRole();

        Set<Role> roles = new HashSet<>();
        for(String role : strRoles) {
            Role roleFound = roleRepository.findByName(RoleName.valueOf(role.toUpperCase())).orElseThrow(() -> new ResourceNotFoundException("Not found any role!"));
            roles.add(roleFound);
        }

        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User getUserById(Long id){

        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + id));
    }

    public User getUserByUsername(String username){

        return userRepository.findUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found for this username: " + username));
    }

    public User getUserByEmail(String email){

        return userRepository.findUserByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found for this email: " + email));
    }


    public User updateUser(String username, User user) {

        User userFound = getUserByUsername(username);

        User userUpdated = updateUser(userFound, user);

        return userRepository.save(userUpdated);
    }

    public Map<String, Boolean> deleteUser(Long id) {

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

        AndFilter searchCriteria = new AndFilter(new FilterByName(), new FilterByGender(), new FilterByAge(), new FilterByRating(), new FilterByCity());
        if(!isNameIsEmpty || !isGenderIsUndefined || !isAgeIsZero || !isCityIsEmpty || !isRatingIsZero) {
            users = searchCriteria.filterUsers(users, filterForm, sort);
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