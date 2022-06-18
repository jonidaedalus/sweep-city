package kz.alash.naklei.validation;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.security.entity.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class UserExistsValidator implements ConstraintValidator<CheckUserExists, String> {
//    private String message;

    @Override
    public void initialize(CheckUserExists constraintAnnotation) {
//        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        long existing = AppBeans.get(DataManager.class).getCount(LoadContext.create(User.class).setQuery(
                LoadContext.createQuery("select u from sec$User u where u.loginLowerCase = :login")
                        .setParameter("login", value.toLowerCase())));

        boolean isValid = existing < 1;

//        if(!isValid){
//            context.disableDefaultConstraintViolation();
//
//            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
//        }

        return isValid;
    }
}