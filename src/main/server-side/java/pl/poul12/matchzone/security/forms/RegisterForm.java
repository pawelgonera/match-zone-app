package pl.poul12.matchzone.security.forms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.poul12.matchzone.constraint.FieldName;
import pl.poul12.matchzone.constraint.Match;
import pl.poul12.matchzone.constraint.NotExist;
import pl.poul12.matchzone.model.enums.Gender;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.*;

@ToString
@Setter
@Getter
@Match(firstField = "password", secondField = "repeatedPassword", message = "Passwords must be the same")
public class RegisterForm implements Serializable {
    @NotBlank
    @Size(min = 4, max = 20)
    @NotExist(fieldName = FieldName.USERNAME, message = "This username is already exist")
    private String username;

    @NotBlank
    @Size(min = 3, max = 20)
    private String name;

    @NotBlank
    @Size(max = 30)
    @Email
    @NotExist(fieldName = FieldName.EMAIL, message = "That email is already exist")
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 25)
    private String password;

    @NotBlank
    @Size(min = 6, max = 25)
    private String repeatedPassword;

    @NotNull
    @Past(message = "Date must be in past")
    private LocalDate dateOfBirth;

    @NotNull
    @Min(value = 18, message = "Age must not be less than 18")
    @Max(value = 75, message = "Age must not be greater than 75")
    private Integer age;

    @NotNull
    private Gender gender;

}