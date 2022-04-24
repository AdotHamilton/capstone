package com.teksystems.hamilton.austin.capstone.controller;


import com.teksystems.hamilton.austin.capstone.database.dao.ServiceDAO;
import com.teksystems.hamilton.austin.capstone.database.dao.UserRoleDAO;
import com.teksystems.hamilton.austin.capstone.database.service.UserService;
import com.teksystems.hamilton.austin.capstone.entity.Service;
import com.teksystems.hamilton.austin.capstone.entity.User;
import com.teksystems.hamilton.austin.capstone.fileUtils.FileStorageService;
import com.teksystems.hamilton.austin.capstone.formbean.ServiceFormBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/services")
public class ServiceController {
    @Autowired
    private ServiceDAO serviceDAO;

    @Autowired
    private UserService userService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private UserRoleDAO userRoleDAO;


    @PostMapping(value = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity createAdvert(@ModelAttribute ServiceFormBean form) throws IOException {
        MultipartFile file = form.getFile();
        log.info(form.getFile().getOriginalFilename());
        User u = userService.findById(form.getUser_id());
        List<String> roles =  userService.getUserRoles(u.getId());

        if(u.getId() != null && roles.contains("BUSINESS")){
            Service s = new Service();
            log.info("creating service");
            s.setProvider(u);
            if(file != null){
                log.info("file: " + file.getOriginalFilename());
                String fileName = fileStorageService.saveFile(file, "services/" + u.getId());
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/files/services/"+ u.getId() + "/")
                        .path(fileName)
                        .toUriString();
                s.setImage(fileDownloadUri);
            }
            s.setTitle(form.getTitle());
            s.setDescription(form.getDescription());

            Service saved = serviceDAO.save(s);
            return new ResponseEntity(saved, HttpStatus.OK);
        }
        log.info(form.toString());
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    @GetMapping("")
    public ResponseEntity getServices(){
        List<Service> services = serviceDAO.findAll();
        return new ResponseEntity(services, HttpStatus.OK);
    }


}
