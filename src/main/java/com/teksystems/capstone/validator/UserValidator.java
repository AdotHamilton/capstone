package com.teksystems.capstone.validator;

import com.teksystems.capstone.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

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
        if(!u.getPassword().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).+")){
            System.out.println("password doesnt match regex");
            errors.rejectValue("password", "password", "Password must contain: 1 Uppercase, 1 Lowercase, 1 number, and 1 special character.");
        }
    }


}
