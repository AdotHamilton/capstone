package com.teksystems.hamilton.austin.capstone.controller;

import com.teksystems.hamilton.austin.capstone.database.dao.EventDAO;
import com.teksystems.hamilton.austin.capstone.database.dao.UserDAO;
import com.teksystems.hamilton.austin.capstone.entity.Event;
import com.teksystems.hamilton.austin.capstone.entity.User;
import com.teksystems.hamilton.austin.capstone.fileUtils.FileStorageService;
import com.teksystems.hamilton.austin.capstone.formbean.EventFormBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/events")
public class MeetController {
    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private UserDAO userDAO;

    @RequestMapping(path="/create", method= RequestMethod.POST, consumes ={ MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity createMeet(@Valid @ModelAttribute EventFormBean form, BindingResult bindingResult) throws IOException {
        MultipartFile file = form.getFile();
        User u = userDAO.findUserById(form.getUserId());
        if(u != null){
            if(bindingResult.hasErrors()){
                Map<String, String> errors = new HashMap<>(); // create key/value for form fields and error messages
                for(FieldError error : bindingResult.getFieldErrors()){
                    errors.put(error.getField(), error.getDefaultMessage());
                }
                return new ResponseEntity<>(errors, HttpStatus.OK); // return as JSON
                // this logic maps my errors to their respective fields in the frontend
            }
            Event e = new Event();
            e.setCreator(u);
            if(file != null){
                log.info("file: " + file.getOriginalFilename());
                String fileName = fileStorageService.saveFile(file, "events/" + u.getId());
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/files/events/"+ u.getId() + "/")
                        .path(fileName)
                        .toUriString();
                e.setBannerUrl(fileDownloadUri);
            }
            e.setTitle(form.getTitle());
            e.setAddress(form.getAddress());
            Date newDate = new Date(Long.parseLong(form.getDate())); // get Milliseconds since jan 1 1970 -- convert to Java Date format
            e.setDate(newDate);
            e.setDescription(form.getDescription());
            e.setState(form.getState());
            Event newEvent = eventDAO.save(e);
            return new ResponseEntity(newEvent, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    @RequestMapping(value = "")
    public ResponseEntity getMeets() {
        List<Event> events = eventDAO.findAll();
        return new ResponseEntity(events, HttpStatus.OK);
    }

    @RequestMapping(value = "findById/{id}")
    public ResponseEntity getMeetById(@PathVariable Long id){
        Event e = eventDAO.findEventById(id);
        return new ResponseEntity(e, HttpStatus.OK);
    }

    @RequestMapping(value = "findByState/{state}", method = RequestMethod.GET)
    public ResponseEntity getMeetsByState(@PathVariable String state) {
        List<Event> events = eventDAO.findByState(state);
        return new ResponseEntity(events, HttpStatus.OK);
    }



}
