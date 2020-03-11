package pl.poul12.matchzone.service.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.forms.FilterForm;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FilterByName implements Filter {

    @Override
    public List<User> filterUsers(List<User> users, FilterForm filterForm, Sort sort) {
        final String pattern = "^" + filterForm.getName().toUpperCase() + ".*$";
         return users.stream()
                    .filter(user -> user.getFirstName().toUpperCase().matches(pattern))
                    .collect(Collectors.toList());
    }
}
