package pl.poul12.matchzone.service.filter;

import org.springframework.data.domain.Sort;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.model.forms.FilterForm;

import java.util.Collections;
import java.util.List;

public class AndFilter implements Filter {

    private Filter[] filters;

    public AndFilter(Filter... filters) {
        this.filters = filters;
    }

    @Override
    public List<User> filterUsers(List<User> users, FilterForm filterForm, Sort sort) {
        List<User> filteredUsers;

        for(Filter filter : filters){
            filteredUsers = filter.filterUsers(users, filterForm, sort);
            if(!filteredUsers.isEmpty()) {
                users = filter.filterUsers(users, filterForm, sort);
            }
            /*else {
                return Collections.emptyList();
            }*/
        }

        return users;
    }

}
