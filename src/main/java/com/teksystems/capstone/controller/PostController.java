package com.teksystems.capstone.controller;

import com.teksystems.capstone.database.dao.PostDAO;
import com.teksystems.capstone.database.dao.UserRoleDAO;
import com.teksystems.capstone.entity.UserFollowing;
import com.teksystems.capstone.fileUtils.FileStorageService;
import com.teksystems.capstone.database.service.PostService;
import com.teksystems.capstone.database.service.UserService;
import com.teksystems.capstone.entity.Post;
import com.teksystems.capstone.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
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
    //Create a post
    @PostMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity createPost(
                            @PathVariable Long id,
                            @RequestParam(required = false) String content,
                            @RequestParam MultipartFile file) throws IOException {
        User u = userService.findById(id); // get the user for the post owner
        if(u != null ) { // if the user exists
            log.info("Creating post from: " +u.toString());
            Post newPost = new Post(); // empty post
            newPost.setCreator(u);
            if (!file.isEmpty()) { // AND if a file was submitted
                String fileName = fileStorageService.saveFile(file, "posts/" + u.getId());
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/files/posts/"+ u.getId() + "/")
                        .path(fileName)
                        .toUriString();
                newPost.setImage(fileDownloadUri);
            }
            if(!content.isBlank()){
                newPost.setContent(content);
            }
            Post savedPost = postDAO.save(newPost);
            return new ResponseEntity<Post>(savedPost,HttpStatus.OK);
        } // if there is no creator_id dont save, return null.
        else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value ="")
    public ResponseEntity getAllPosts(){
        List<Post> postList = postDAO.findAll(Sort.by(Sort.Order.desc("date")));
        return new ResponseEntity(postList, HttpStatus.OK);
    }
    @GetMapping(value="/getPostsForCreator/{id}")
    public List<Post> getPostsByCreator(@PathVariable Long id){
        List<Post> postList = postService.findPostsForCreator(id);
        return postList;
    }
    @GetMapping(value="/getFollowingPosts")
    public List<Post> getPostsForFollowingUsers(@RequestParam long user_id, @RequestParam int length){
        List<UserFollowing> followingList = userService.findFollowedUsers(user_id); // get list of followed users from join table
        log.info("start: 0" );
        log.info("end: " + length);
        List<User> usersFollowed = new ArrayList<>(); // construct list of Users out of UserFollowing Class
        for(UserFollowing userFollowing : followingList){
            usersFollowed.add(userFollowing.getFollowedUser()); // for each row in join table add the FollowedUser
        }
        if(!usersFollowed.isEmpty()){
            usersFollowed.add(followingList.get(0).getFollowingUser());
        }
        List<Post> postList = postDAO.findAllPostsByCreatorInOrderByDateDesc(usersFollowed);
        if(postList.size() > 10){
            return postList.subList(0, 10);
        }
        return postList;
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
