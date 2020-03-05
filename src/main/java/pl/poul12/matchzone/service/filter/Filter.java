package pl.poul12.matchzone.service.filter;

import org.springframework.data.domain.Sort;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.forms.FilterForm;

import java.util.List;

public interface Filter {

    List<User> filterUsers(List<User> users, FilterForm filterForm, Sort sort);
}
