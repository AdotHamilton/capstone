package com.teksystems.capstone.database.dao;

import com.teksystems.capstone.entity.Post;
import com.teksystems.capstone.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDAO extends JpaRepository<Post, Long> {
    public Post findPostById(@Param("post_id") Long post_id);
    public List<Post> findPostByCreatorOrderByDateDesc(@Param("creator") User u);

    public List<Post> findAllPostsByCreatorInOrderByDateDesc(@Param("creator") List<User> u);
}
