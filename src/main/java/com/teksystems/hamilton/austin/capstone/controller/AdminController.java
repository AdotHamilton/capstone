package com.teksystems.hamilton.austin.capstone.controller;

import com.teksystems.hamilton.austin.capstone.database.dao.UserDAO;
import com.teksystems.hamilton.austin.capstone.database.dao.UserRoleDAO;
import com.teksystems.hamilton.austin.capstone.database.service.UserService;
import com.teksystems.hamilton.austin.capstone.entity.LoginForm;
import com.teksystems.hamilton.austin.capstone.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleDAO userRoleDAO;
    @Autowired
    private UserDAO userDAO;


//        if(userService.validateLogin(form)){
//            User u = userService.findByEmail(form.getEmail());
//            response.addObject("user", u);
//            session.setAttribute("user", u);
//            response.setViewName("index.jsp");
//        } else {
//            response.setViewName("AdminLogin.jsp");
//        }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView adminLoginPage(){
        ModelAndView response = new ModelAndView();
        LoginForm loginForm = new LoginForm();

        response.addObject("form", loginForm);
        response.setViewName("AdminLogin.jsp");
        return response;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value="/portal")
    public ModelAndView adminPortal(){
        ModelAndView response = new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User admin = userService.findByEmail(username);
        log.debug(username);
        response.addObject("user", admin);
        response.setViewName("index.jsp");
        return response;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value="/users")
    public ModelAndView users() {
        ModelAndView res = new ModelAndView();
        List<Map<String, Object>> userList = userRoleDAO.findUsersAndRoles();
        log.info(userList.get(0).keySet().toString());
        res.addObject("userList", userList);
        res.setViewName("users.jsp");
        return res;
    }



    @RequestMapping(value="/error/404")
    public ModelAndView errorPage(){
        ModelAndView res = new ModelAndView();

        res.setViewName("errorPage.jsp");
        return res;
    }

}
