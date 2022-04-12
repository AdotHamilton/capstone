package com.teksystems.capstone.database.service;

import com.teksystems.capstone.database.dao.UserDAO;
import com.teksystems.capstone.entity.LoginForm;
import com.teksystems.capstone.entity.User;
import com.teksystems.capstone.validator.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.validation.BindingResult;

import java.util.List;
@Service
@Slf4j
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserValidator userValidator;

    // find user methods
    public List<User> findAllUsers(){
        return userDAO.findAll();
    }
    public User findUserByDisplayName(String displayName){
        return userDAO.findUserByDisplayName(displayName);
    }
    public User findByEmail(String email){
        return userDAO.findUserByEmail(email);
    }
    public User findById(Long id){
        return userDAO.findUserById(id);
    }
    public User updateUser(User u){
        return userDAO.save(u);
    }
    public List<User> searchUsersByDisplayName(String search){
        return userDAO.findUsersByDisplayNameContaining(search).subList(0, 10);
    }


    public User registerUser(User u){
        if(findByEmail(u.getEmail()) == null){
            log.info("user info is valid");
            String hash = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
            u.setPassword(hash);
            return userDAO.save(u);
        }
        else return null;
    }
    public boolean validateLogin(LoginForm loginForm){
        User u = findByEmail(loginForm.getEmail()); // getting user from email in login
        log.info(loginForm.getEmail() + " | " +loginForm.getPassword());
        if(u != null){
            return BCrypt.checkpw(loginForm.getPassword(), u.getPassword());
        } else {
            return false;
        }
    }

}








