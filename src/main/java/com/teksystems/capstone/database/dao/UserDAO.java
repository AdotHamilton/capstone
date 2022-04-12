package com.teksystems.capstone.database.dao;

import com.teksystems.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    public User findUserByEmail(@Param("email") String email);
    public User findUserById(@Param("id") Long id);
    public User findUserByDisplayName(@Param("displayName") String displayName);
    public List<User> findUsersByDisplayNameContaining(@Param("display_name") String display_name);


}
