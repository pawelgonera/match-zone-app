package pl.poul12.matchzone.service.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.forms.FilterForm;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FilterByAge implements Filter {

    @Override
    public List<User> filterUsers(List<User> users, FilterForm filterForm, Sort sort) {
        return users.stream()
                    .filter(user -> user.getPersonalDetails().getAge() >= filterForm.getAgeMin() && user.getPersonalDetails().getAge() <= filterForm.getAgeMax())
                    .collect(Collectors.toList());
    }
}
