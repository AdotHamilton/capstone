package com.teksystems.hamilton.austin.capstone.database.service;

import com.teksystems.hamilton.austin.capstone.database.dao.PostDAO;
import com.teksystems.hamilton.austin.capstone.database.dao.UserDAO;
import com.teksystems.hamilton.austin.capstone.entity.Post;
import com.teksystems.hamilton.austin.capstone.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PostService {
    @Autowired
    PostDAO postDAO;

    @Autowired
    UserDAO userDAO;

    public List<Post> findPostsForCreator(Long id){
        User u = userDAO.findUserById(id);
        if(u != null){
            return postDAO.findPostByCreatorOrderByDateDesc(u);
        } else {
            return null;
        }
    }

}
