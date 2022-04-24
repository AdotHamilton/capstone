package com.teksystems.hamilton.austin.capstone.controller;

import com.teksystems.hamilton.austin.capstone.database.dao.UserFollowingDAO;
import com.teksystems.hamilton.austin.capstone.database.dao.UserRoleDAO;
import com.teksystems.hamilton.austin.capstone.database.dao.VehicleDAO;
import com.teksystems.hamilton.austin.capstone.entity.UserRole;
import com.teksystems.hamilton.austin.capstone.fileUtils.FileStorageService;
import com.teksystems.hamilton.austin.capstone.database.service.UserService;
import com.teksystems.hamilton.austin.capstone.entity.LoginForm;
import com.teksystems.hamilton.austin.capstone.entity.User;
import com.teksystems.hamilton.austin.capstone.entity.Vehicle;
// Modules imports
import com.teksystems.hamilton.austin.capstone.formbean.AddVehicleFormBean;
import com.teksystems.hamilton.austin.capstone.formbean.RegisterFormBean;
import com.teksystems.hamilton.austin.capstone.validator.UserValidator;
import lombok.extern.slf4j.Slf4j;
// logging
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserRoleDAO userRoleDAO;
    @Autowired
    private VehicleDAO vehicleDAO;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private UserFollowingDAO userFollowingDAO;

    @GetMapping("/all")
    public List<User> findAllUsers(){
        List<User> users = userService.findAllUsers();
        return users;
    };

    @GetMapping("/findById/{id}")
    public User getUserById(@PathVariable Long id){
        User u = userService.findById(id);
        if(u != null){
            // http status 200
            return u;
        } else {
            // http status failed
            return null;
        }
    }
    @GetMapping("/searchByDisplayName")
    public List<User> searchByDisplayName(@RequestParam String search){
        if(search.length() > 2){
            return userService.searchUsersByDisplayName(search);
        }
        return null;
    }
    @GetMapping("/findByDisplayName/{displayName}")
    public User findByDisplayName(@PathVariable String displayName){
        return userService.findUserByDisplayName(displayName);
    }
    @GetMapping("/testUserRoleById")
    public List<Map<String, Object>> getUserAndRoleById(@RequestParam Long id){
        return userRoleDAO.findUserAndRoleById(id);
    }

    @PostMapping("/follow")
    public ResponseEntity followUser(@RequestParam Long following_user_id, @RequestParam Long followed_user_id){
        Boolean result = userService.followUser(following_user_id, followed_user_id);
        if(result){
            return new ResponseEntity(HttpStatus.OK);
        } else return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterFormBean form, BindingResult result){
        User tempUser = new User();
        if(form.getIsBusiness()){
            log.info("Business");
        } else {log.info("User");}

        tempUser.setEmail(form.getEmail());
        tempUser.setPassword(form.getPassword());
        tempUser.setConfirmPw(form.getConfirmPw());
        tempUser.setDisplayName(form.getDisplayName());
        tempUser.setState(form.getState());
        tempUser.setZipCode(form.getZipCode());
        if(form.getIsBusiness()){
            tempUser.setBusinessAddress(form.getBusinessAddress());
        }
        userValidator.validate(tempUser, result);
        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>(); // create key/value for form fields and error messages
            for(FieldError error : result.getFieldErrors()){
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.OK); // return as JSON
            // this logic maps my errors to their respective fields in the frontend
        }
        else {
            log.info("No Errors");
            User savedUser = userService.registerUser(tempUser); // returns null if email exists
            if(savedUser != null){ // if user email is unique
                UserRole userRole = new UserRole();
                if(form.getIsBusiness()){
                    userRole.setUserRole("BUSINESS");
                } else {
                    userRole.setUserRole("USER");
                }
                userRole.setUserId(savedUser.getId());
                userRoleDAO.save(userRole);
                return new ResponseEntity<>(savedUser, HttpStatus.OK);
            } else {
                Map<String, String> errors = new HashMap<>();
                errors.put("email", "This account already exists");
                return new ResponseEntity<>(errors, HttpStatus.OK);
            }
        }

    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginForm loginForm){
        boolean isValid = userService.validateLogin(loginForm);
        if(isValid){
            User u = userService.findByEmail(loginForm.getEmail());
            List<Map<String, Object>> userAndRole = userRoleDAO.findUserAndRoleById(u.getId());

            return new ResponseEntity<>(userAndRole, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value="/updatePfp", consumes = "multipart/form-data")
    public ResponseEntity updatePfp(@RequestParam Long user_id,
                                    @RequestParam String pwHash,
                                    @RequestParam(value = "pfp")MultipartFile multipartFile) throws IOException {

        User u = userService.findById(user_id);
        if(u != null && u.getPassword().equals(pwHash)){ // if DB password hash is the same as requestParam
            if(!multipartFile.isEmpty()){ // if a file was sumbitted
                Date uploadDate = new Date();
                String uriBuilder = u.getId() + "/" + uploadDate.toString() + "/"; // ex: 2/Fri%20Apr%2022%2013:20:17%20CDT

                String fileName = fileStorageService.saveFile(multipartFile, "users/" + u.getId());
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/files/users/" + uriBuilder)
                        .path(fileName)
                        .toUriString();
                log.info("length: " +fileDownloadUri.length());
                u.setPfp(fileDownloadUri);
            }
            User saved = userService.updateUser(u);
            return new ResponseEntity(saved, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

//    @RequestParam(required = false) String email,
//    @RequestParam(required = false) String displayName,




    @PostMapping(value = "/update", consumes = "application/json")
    public ResponseEntity updateUser(@Valid @RequestBody User request, BindingResult result) throws IOException {
            log.info(request.toString());
            User u = userService.findById(request.getId()); // get user from id parameter in request
            log.info(u.toString());
            if(u.getId() != null ){
                if(result.hasErrors()){
                    log.info("hasErrors");
                    Map<String, String> errors = new HashMap<>(); // create key/value for form fields and error messages
                    for(FieldError error : result.getFieldErrors()){
                        errors.put(error.getField(), error.getDefaultMessage());
                    }
                    return new ResponseEntity<>(errors, HttpStatus.OK); // return as JSON
                } else {
                    if(!request.getEmail().isBlank()){ u.setEmail(request.getEmail());} // if field values were recieved and valid, set them
                    if(!request.getDisplayName().isBlank()){u.setDisplayName(request.getDisplayName()); }
                    if(request.getBusinessAddress() != null && !request.getBusinessAddress().isBlank()){ u.setBusinessAddress(request.getBusinessAddress()); }
                    if(request.getZipCode() != null && !request.getZipCode().isBlank()){ u.setZipCode(request.getZipCode()); }
                    if(request.getState() != null && !request.getState().isBlank()){ u.setState(request.getState()); }
                    User savedUser = userService.updateUser(u);
                    return new ResponseEntity<User>(savedUser, HttpStatus.OK);
                }

            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value="/garage/add")
    public ResponseEntity addVehicle(@RequestBody AddVehicleFormBean form){
        log.info(form.toString());
        User u = userService.findById(form.getUser_id());
        String make = form.getMake();
        String model = form.getModel();
        int year = form.getYear();

        if(u != null) {
            List<Vehicle> vehicleList = vehicleDAO.findVehiclesByMakeAndModelAndYear(make, model, year);
            List<Vehicle> userGarage = u.getVehicles();
            if (vehicleList.isEmpty()) {
                Vehicle v = new Vehicle();
                v.setMake(make);
                v.setModel(model);
                v.setYear(year);
                Vehicle savedVehicle = vehicleDAO.save(v);
                userGarage.add(savedVehicle);
                u.setVehicles(userGarage);
                User savedUser = userService.updateUser(u);
                return new ResponseEntity<User>(savedUser, HttpStatus.OK);
            } else {
                Vehicle v = vehicleList.get(0);
                v.setVIN(null);
                userGarage.add(v);
                u.setVehicles(userGarage);
                User savedUser = userService.updateUser(u);
                return new ResponseEntity<User>(savedUser, HttpStatus.OK);
            }
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
//    @RequestMapping(value="/getByDisplayName/{dName}", method = RequestMethod.GET)
//    public ModelAndView index(@PathVariable String dName) {
//        ModelAndView response = new ModelAndView();
//
//        User singleUser = userService.findUserByDisplayName(dName);
//        if(singleUser!= null){
//            response.addObject("user", singleUser);
//        } else {
//            response.addObject("user", null);
//        }
//
//        response.setViewName("index");
//        return response;
//    }
//    @RequestMapping(value="", method = RequestMethod.GET)
//    public ModelAndView regFormView() {
//        ModelAndView res = new ModelAndView("registerForm", "user", new User());
//
//        return res;
//    }
//    this is the method with JSP/Thymeleaf
//    public String registerUser(@Validated @ModelAttribute("user")User user, BindingResult result, HttpSession session){
//        userValidator.validate(user, result);
//        if(result.hasErrors()){
//            return "registerForm";
//        }
//        userService.registerUser(user);
//        session.setAttribute("user_id", user.getId());
//        return "redirect:/home";
//    }
}
