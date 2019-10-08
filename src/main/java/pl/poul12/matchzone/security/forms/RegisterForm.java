package pl.poul12.matchzone.security.forms;

import lombok.Getter;
import lombok.Setter;
import pl.poul12.matchzone.model.enums.Gender;

import java.util.Set;

import javax.validation.constraints.*;

@Setter
@Getter
public class RegisterForm {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(min = 6, max = 40)
    private String repeatedPassword;

    @NotBlank
    private String dateOfBirth;

    @NotNull
    private Integer age;

    @NotNull
    private Gender gender;

}