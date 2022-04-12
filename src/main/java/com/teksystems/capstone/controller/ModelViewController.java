package com.teksystems.capstone.controller;

import com.teksystems.capstone.database.service.UserService;
import com.teksystems.capstone.entity.LoginForm;
import com.teksystems.capstone.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class ModelViewController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView adminPage(@RequestBody LoginForm form, HttpSession session){
        ModelAndView response = new ModelAndView();
        if(userService.validateLogin(form)){
            User u = userService.findByEmail(form.getEmail());
            response.addObject("user", u);
            session.setAttribute("user", u);
            response.setViewName("index.jsp");
        } else {
            response.setViewName("registerForm.jsp");
        }
        return response;
    }
}
