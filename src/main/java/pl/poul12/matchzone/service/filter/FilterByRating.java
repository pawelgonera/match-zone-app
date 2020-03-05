package pl.poul12.matchzone.service.filter;

import org.springframework.data.domain.Sort;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.forms.FilterForm;

import java.util.List;
import java.util.stream.Collectors;

public class FilterByRating implements Filter {

    @Override
    public List<User> filterUsers(List<User> users, FilterForm filterForm, Sort sort) {
        return users.stream()
                    .filter(user -> user.getPersonalDetails().getRating() >= filterForm.getRatingMin() && user.getPersonalDetails().getRating() <= filterForm.getRatingMax())
                    .collect(Collectors.toList());
    }
}
