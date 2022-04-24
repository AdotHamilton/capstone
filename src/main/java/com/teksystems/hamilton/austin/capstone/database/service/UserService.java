package com.teksystems.hamilton.austin.capstone.database.service;

import com.teksystems.hamilton.austin.capstone.database.dao.UserDAO;
import com.teksystems.hamilton.austin.capstone.database.dao.UserFollowingDAO;
import com.teksystems.hamilton.austin.capstone.database.dao.UserRoleDAO;
import com.teksystems.hamilton.austin.capstone.entity.LoginForm;
import com.teksystems.hamilton.austin.capstone.entity.User;
import com.teksystems.hamilton.austin.capstone.entity.UserFollowing;
import com.teksystems.hamilton.austin.capstone.entity.UserRole;
import com.teksystems.hamilton.austin.capstone.validator.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserFollowingDAO userFollowingDAO;
    @Autowired
    private UserRoleDAO userRoleDAO;


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
        log.info(search);
        return userDAO.findUsersByDisplayNameContaining(search);
    }
    public List<UserFollowing> findFollowedUsers(Long user_id){
        User u = userDAO.findUserById(user_id);
        if(u != null){
            return userFollowingDAO.findByFollowingUser_Id(user_id);
        } else {
            return null;
        }
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

    public List<String> getUserRoles(Long id){
        List<UserRole> roles = userRoleDAO.findUserRoleByUserId(id);
        List<String> roleStrings = new ArrayList();
        for(UserRole role : roles){
            roleStrings.add(role.getUserRole());
        }
        return roleStrings;
    }

    public boolean followUser(Long following_id, Long followed_id){
        User following = findById(following_id);
        User followed = findById(followed_id);
        if(following.getId() != null && followed.getId() != null){
            UserFollowing userFollowing = new UserFollowing();
            userFollowing.setFollowingUser(following);
            userFollowing.setFollowedUser(followed);
            userFollowingDAO.save(userFollowing);
            return true;
        } else {
            return false;
        }
    }


}








