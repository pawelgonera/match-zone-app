package pl.poul12.matchzone.constraint;

import pl.poul12.matchzone.service.UserServiceImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotExistValidator implements ConstraintValidator<NotExist, String> {

    private UserServiceImpl userServiceImpl;

    private FieldName fieldName;

    public NotExistValidator(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public void initialize(NotExist constraintAnnotation) {

        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

       switch (fieldName){
           case USERNAME:
               return userServiceImpl.getUserByUsername(value) != null;
           case EMAIL:
               return userServiceImpl.getUserByEmail(value) != null;
       }

       return false;
    }
}
