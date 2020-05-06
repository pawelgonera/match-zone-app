package pl.poul12.matchzone.constraint;

import pl.poul12.matchzone.security.forms.UserValidationData;
import pl.poul12.matchzone.service.UserServiceImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class IsOldPasswordValidator implements ConstraintValidator<IsOldPass, UserValidationData> {

    private UserServiceImpl userService;

    private String usernameField;
    private String passwordField;

    public IsOldPasswordValidator(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(IsOldPass constraintAnnotation) {
        this.usernameField = constraintAnnotation.username();
        this.passwordField = constraintAnnotation.password();
    }

    @Override
    public boolean isValid(UserValidationData value, ConstraintValidatorContext context) {
        try {
            Field field1 = value.getClass().getDeclaredField(usernameField);
            field1.setAccessible(true);
            Field field2 = value.getClass().getDeclaredField(passwordField);
            field2.setAccessible(true);

            String username = field1.get(value).toString();
            String password = field2.get(value).toString();

            return userService.isPasswordMatch(username, password);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }
}
