package com.teksystems.hamilton.austin.capstone.controller;

import com.teksystems.hamilton.austin.capstone.database.dao.PostDAO;
import com.teksystems.hamilton.austin.capstone.entity.UserFollowing;
import com.teksystems.hamilton.austin.capstone.fileUtils.FileStorageService;
import com.teksystems.hamilton.austin.capstone.database.service.PostService;
import com.teksystems.hamilton.austin.capstone.database.service.UserService;
import com.teksystems.hamilton.austin.capstone.entity.Post;
import com.teksystems.hamilton.austin.capstone.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity getPostsForFollowingUsers(@RequestParam long user_id,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "9") int size){
        List<UserFollowing> followingList = userService.findFollowedUsers(user_id); // get list of followed users from join table
        log.info("page:" + page );
        log.info("size: " + size );
        List<User> usersFollowed = new ArrayList<>(); // construct list of Users out of UserFollowing Class
        usersFollowed.add(userService.findById(user_id)); // add self to list
        for(UserFollowing userFollowing : followingList){
            usersFollowed.add(userFollowing.getFollowedUser()); // for each row in join table add the FollowedUser
        }
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Post> postsPage = postDAO.findAllPostsByCreatorInOrderByDateDesc(usersFollowed, paging);
            List<Post> postList = postsPage.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("posts", postList);
            response.put("currentPage", postsPage.getNumber());
            response.put("totalItems", postsPage.getTotalElements());
            response.put("totalPages", postsPage.getTotalPages());
            return new ResponseEntity(response, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

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
