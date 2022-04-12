package com.teksystems.capstone.database.dao;

import com.teksystems.capstone.entity.Post;
import com.teksystems.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostDAO extends JpaRepository<Post, Long> {
    public Post findPostById(@Param("post_id") Long post_id);
    public List<Post> findPostByCreator(@Param("creator") User u);
}
