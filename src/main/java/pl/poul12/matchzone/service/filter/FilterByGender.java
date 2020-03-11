package pl.poul12.matchzone.service.filter;

import org.springframework.data.domain.Sort;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.forms.FilterForm;

import java.util.List;
import java.util.stream.Collectors;

public class FilterByGender implements Filter {

    @Override
    public List<User> filterUsers(List<User> users, FilterForm filterForm, Sort sort) {
        if (filterForm.getGender().ordinal() == 0) {
            return users;
        } else {
            return users.stream()
                    .filter(user -> user.getPersonalDetails().getGender() == filterForm.getGender())
                    .collect(Collectors.toList());
        }
    }
}
