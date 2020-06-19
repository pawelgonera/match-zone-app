package pl.poul12.matchzone.service;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.forms.FilterForm;
import pl.poul12.matchzone.security.forms.RegisterForm;;

import java.io.IOException;
import java.util.*;

public interface UserService {

    List<User> getAllUsers();

    List<User> getAllUsersBySort(Sort sort);

    User saveUser(User user);

    User createUser(RegisterForm registerUser);

    User getUserById(Long id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    User updateUser(String username, User user);

    void changeAvatar(String username, MultipartFile file) throws IOException;

    Map<String, Boolean> deleteUser(Long id);

    PagedListHolder<User> filterUserList(FilterForm filterForm);

    boolean isPasswordMatch(String username, String password);
}
