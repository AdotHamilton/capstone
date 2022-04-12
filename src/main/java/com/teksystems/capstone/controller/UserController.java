package com.teksystems.capstone.controller;

import com.teksystems.capstone.database.dao.VehicleDAO;
import com.teksystems.capstone.fileUtils.FileStorageService;
import com.teksystems.capstone.database.service.UserService;
import com.teksystems.capstone.entity.LoginForm;
import com.teksystems.capstone.entity.User;
import com.teksystems.capstone.entity.Vehicle;
// Modules imports
import com.teksystems.capstone.validator.UserValidator;
import lombok.extern.slf4j.Slf4j;
// logging
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private VehicleDAO vehicleDAO;
    @Autowired
    private FileStorageService fileStorageService;

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
    @PostMapping("/searchByDisplayName")
    public List<User> searchByDisplayName(@RequestParam String search){
        return userService.searchUsersByDisplayName(search);
    }
    @GetMapping("/findByDisplayName/{displayName}")
    public User findByDisplayName(@PathVariable String displayName){
        return userService.findUserByDisplayName(displayName);
    }


    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody User u, BindingResult result){
        log.info(u.toString());
        // user service will check if the email already exists
        // and return null if it DOES exist
        userValidator.validate(u, result);
        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>(); // trying to create Field: ErrorMsg json response to send backend validations to React
//                    result.getAllErrors().stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .collect(Collectors.toList());
            log.info(result.getFieldErrors().stream().spliterator().toString());
            return new ResponseEntity<>(errors, HttpStatus.OK); 

            }
        else {
            log.info("No Errors");
            User savedUser = userService.registerUser(u);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);

        }

    }
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginForm loginForm){
        boolean isValid = userService.validateLogin(loginForm);
        if(isValid){
            User u = userService.findByEmail(loginForm.getEmail());
            return new ResponseEntity<User>(u, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping(value = "/update", consumes = "multipart/form-data")
    public ResponseEntity updateUser(
            @RequestParam Long id,
//            @RequestParam(required = false) String email,
//            @RequestParam(required = false) String displayName,
            @RequestParam(value = "pfp", required = false)MultipartFile multipartFile) throws IOException {

            User u = userService.findById(id); // get user from id parameter in request
            if(u != null ){
                if(!multipartFile.isEmpty()){ // if a file was sumbitted
                    String fileName = fileStorageService.saveFile(multipartFile, "users/" + u.getId()); // saveFile(file, subdirectory = "users/{id}")
                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath() // building Uri to serve the file out of filesystem
                            .path("/files/users/"+ u.getId() + "/") // full route = http://localhost:8080/api/files/users/{id}/filename
                            .path(fileName)
                            .toUriString();
                    u.setPfp(fileDownloadUri); // setting users PFP varchar field to the uri
                }
                User savedUser = userService.updateUser(u); // updating user with DAO.save(u)
                return new ResponseEntity<User>(savedUser, HttpStatus.OK); // return newly updated user
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value="/garage/add")
    public ResponseEntity addVehicle(@RequestParam Long user_id, @RequestParam Integer year, @RequestParam String make, @RequestParam String model){
        User u = userService.findById(user_id);
        if(u != null) {
            List<Vehicle> vehicleList = vehicleDAO.findVehiclesByMakeAndModelAndYear(make, model, year); 
            List<Vehicle> userGarage = u.getVehicles();
            if (vehicleList.isEmpty()) { // if vehicle is not in the DB already
                Vehicle v = new Vehicle();
                v.setMake(make);
                v.setModel(model);
                v.setYear(year);
                vehicleDAO.save(v); // create and save to DB
                userGarage.add(v); 
                u.setVehicles(userGarage); // add vehicle to users_vehicles table
                User savedUser = userService.updateUser(u); 
                return new ResponseEntity<User>(savedUser, HttpStatus.OK);
            } else { // if vehicle is in DB
                Vehicle v = vehicleList.get(0); // get it 
                v.setVIN(null); // set VIN field to null
                userGarage.add(v); // add it to users_vehicles table
                u.setVehicles(userGarage);
                User savedUser = userService.updateUser(u); 
                return new ResponseEntity<User>(savedUser, HttpStatus.OK); // return updated user
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
