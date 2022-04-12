package com.teksystems.capstone.controller;

import com.teksystems.capstone.fileUtils.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("files")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/users/{id}/{filename:.+}")
    public ResponseEntity<Resource> getPfp(@PathVariable String filename, @PathVariable Long id, HttpServletRequest request){
        Resource resource = fileStorageService.loadFileAsResource(filename, "users/" + id); // find file in system, C:/uploads/ + users/{id}/ + filename 

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath()); // I dont even know
        } catch (IOException e) {
           log.info("Couldnt determine filetype");
        }

       if(contentType == null){
           contentType = "application/octet-stream";
       } 
        // probably add some validation not to return a file unless it is a JPG, PNG etc...
        String mediaType = MediaType.parseMediaType(contentType);
        return ResponseEntity.ok().contentType(mediaType).body(resource); // return file resource
    }
    @GetMapping("/posts/{id}/{filename:.+}")
    public ResponseEntity<Resource> getPost(@PathVariable String filename, @PathVariable Long id, HttpServletRequest request){
        Resource resource = fileStorageService.loadFileAsResource(filename, "posts/" + id); // load file from C:/uploads/posts/{id}/filename

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            log.info("Couldnt determine filetype");
        }

        if(contentType == null){
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

}
