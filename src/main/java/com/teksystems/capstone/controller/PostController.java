package com.teksystems.capstone.controller;

import com.teksystems.capstone.database.dao.PostDAO;
import com.teksystems.capstone.fileUtils.FileStorageService;
import com.teksystems.capstone.database.service.PostService;
import com.teksystems.capstone.database.service.UserService;
import com.teksystems.capstone.entity.Post;
import com.teksystems.capstone.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private UserService userService;
    @Autowired
    PostDAO postDAO;
    @Autowired
    PostService postService;
    @Autowired
    FileStorageService fileStorageService;
//    Create a post
    @PostMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity createPost(
                            @PathVariable Long id, // change this to include some validation so attackers cant create posts for another user
                            @RequestParam MultipartFile file) throws IOException {
        User u = userService.findById(id); // get the user for the post owner
        if(u != null ) { // if the user exists
            log.info("Creating post from: " +u.getDisplayName());
            Post newPost = new Post(); // empty post
            newPost.setCreator(u);
            if (!file.isEmpty()) { // if a file was sumbitted
                String fileName = fileStorageService.saveFile(file, "posts/" + u.getId()); // save file to C:/uploads/posts/{user_id}/fileName
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath() // create url to access file
                        .path("/files/posts/"+ u.getId() + "/") // localhost:8080/api/files/posts/{user_id}/filename
                        .path(fileName)
                        .toUriString();
                newPost.setImage(fileDownloadUri); // set Post varchar() image field to this url
            }
            Post savedPost = postDAO.save(newPost);
            return new ResponseEntity<Post>(savedPost,HttpStatus.OK); // return saved post
        } // if there is no creator_id dont save, return null.
        else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value ="")
    public List<Post> getAllPosts(){
        List<Post> postList = postDAO.findAll();
        return postList;
    }
    @GetMapping(value="/getPostsForCreator/{id}") // later add validation to check if user profile is private
    public List<Post> getPostsByCreator(@PathVariable Long id){ // when viewing user profile page
        return postService.findPostsForCreator(id);
    }


//    @PostMapping(value="/like/{post_id}")
//    public ResponseEntity likePost(@PathVariable Long post_id, @RequestParam Long user_id){
//        Post likedPost = postDAO.findPostById(post_id);
//        User userLiking = userService.findById(user_id);
//        if(likedPost != null){
//            likedPost.getLikes().add(userLiking);
//        }
//    }



}
