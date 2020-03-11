package pl.poul12.matchzone.service.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.forms.FilterForm;

import java.util.Collections;
import java.util.List;

@Slf4j
public class AndFilter implements Filter {

    private Filter[] filters;

    public AndFilter(Filter... filters) {
        this.filters = filters;
    }

    @Override
    public List<User> filterUsers(List<User> users, FilterForm filterForm, Sort sort) {
        List<User> filteredUsers;
        int i = 0;
            for (Filter filter : filters) {
                String filterName = filter.getClass().getSimpleName();
                filteredUsers = filter.filterUsers(users, filterForm, sort);

                if (!filteredUsers.isEmpty()) {
                    i++;
                    users = filteredUsers;
                    log.info("filter: {}, filtered users: {}, i: {}", filterName, filteredUsers.size(), i);
                }
                else {
                    return Collections.emptyList();
                }
            }

        return users;
    }

}
