package pl.poul12.matchzone.constraint;

import pl.poul12.matchzone.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotExistValidator implements ConstraintValidator<NotExist, String> {

    private UserService userService;

    private FieldName fieldName;

    public NotExistValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(NotExist constraintAnnotation) {

        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

       switch (fieldName){
           case USERNAME:
               return !userService.getUserByUsername(value).isPresent();
           case EMAIL:
               return !userService.getUserByEmail(value).isPresent();
       }

       return false;
    }
}
