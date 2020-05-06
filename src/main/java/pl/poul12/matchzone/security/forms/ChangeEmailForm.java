package pl.poul12.matchzone.security.forms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.poul12.matchzone.constraint.IsOldPass;
import pl.poul12.matchzone.constraint.Match;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ToString
@Setter
@Getter
@IsOldPass(username = "username", password = "password", message = "Password is not correct")
public class ChangeEmailForm extends UserValidationData implements Serializable {
    @NotBlank
    @Size(min = 4, max = 20)
    //@NotExist(fieldName = FieldName.USERNAME, message = "This username is already exist")
    private String username;

    @NotBlank
    @Size(max = 30)
    @Email
    //@NotExist(fieldName = FieldName.EMAIL, message = "That email is already exist")
    private String email;

    @NotBlank
    @Size(min = 6, max = 25)
    private String password;
}