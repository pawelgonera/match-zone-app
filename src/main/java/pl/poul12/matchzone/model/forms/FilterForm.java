package pl.poul12.matchzone.model.forms;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.poul12.matchzone.model.enums.Gender;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FilterForm {

    @NotNull
    private String name;
    @NotNull
    private Gender gender;
    @NotNull
    private Integer ageMin;
    @NotNull
    private Integer ageMax;
    @NotNull
    private String city;
}
