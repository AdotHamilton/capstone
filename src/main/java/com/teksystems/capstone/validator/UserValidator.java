package com.teksystems.capstone.validator;

import com.teksystems.capstone.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User u = (User) target;
        if(!u.getConfirmPw().equals(u.getPassword())){
            errors.rejectValue("confirmPw", "Match", "Passwords Must match");
        }
    }


}
