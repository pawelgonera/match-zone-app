package pl.poul12.matchzone.model.forms;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PageUser {

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
