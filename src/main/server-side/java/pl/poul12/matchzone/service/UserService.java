package pl.poul12.matchzone.service;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.forms.FilterForm;
import pl.poul12.matchzone.security.forms.RegisterForm;;

import java.util.*;

public interface UserService {

    List<User> getAllUsers();

    List<User> getAllUsersBySort(Sort sort);

    Page<User> getPageableListOfUsers(Pageable pageable);

    User saveUser(User user);

    User createUser(RegisterForm registerUser);

    User getUserById(Long id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    //ResponseEntity<?> resetPassword(String email);

    //ResponseEntity<?> savePhoto(String username, MultipartFile file);

    User updateUser(String username, User user);

    Map<String, Boolean> deleteUser(Long id);

    PagedListHolder<User> filterUserList(FilterForm filterForm);
}
