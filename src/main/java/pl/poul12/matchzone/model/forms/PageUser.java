package pl.poul12.matchzone.model.forms;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poul12.matchzone.model.User;

import java.util.List;

@NoArgsConstructor
@Data
public class PageUser {

    private List<User> content;
    private Integer page;
    private Integer size;
    private String direction;
    private String sort;
    private Integer totalPages;
    private Integer totalElements;
    private Boolean last;
    private Boolean first;
    private Integer numberOfElements;
}
