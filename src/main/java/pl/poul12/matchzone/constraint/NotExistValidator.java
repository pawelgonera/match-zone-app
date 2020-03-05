package pl.poul12.matchzone.constraint;

import pl.poul12.matchzone.exception.ResourceNotFoundException;
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
               try{
                   userServiceImpl.getUserByUsername(value);
                   return false;
               }catch (ResourceNotFoundException e){
                   return true;
               }
           case EMAIL:
               try{
                   userServiceImpl.getUserByEmail(value);
                   return false;
               }catch (ResourceNotFoundException e){
                   return true;
               }
       }

       return false;
    }
}
