package pl.poul12.matchzone.security.forms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.poul12.matchzone.constraint.IsOldPass;
import pl.poul12.matchzone.constraint.Match;

import java.io.Serializable;
import javax.validation.constraints.*;

@ToString
@Setter
@Getter
@Match(firstField = "password", secondField = "repeatedPassword", message = "Passwords must be the same")
@IsOldPass(username = "username", password = "oldPassword")
public class ChangePasswordForm extends UserValidationData implements Serializable{
    @NotBlank
    @Size(min = 4, max = 20)
    //@NotExist(fieldName = FieldName.USERNAME, message = "This username is already exist")
    private String username;

    @NotBlank
    @Size(min = 6, max = 25)
    private String oldPassword;

    @NotBlank
    @Size(min = 6, max = 25)
    private String password;

    @NotBlank
    @Size(min = 6, max = 25)
    private String repeatedPassword;
}