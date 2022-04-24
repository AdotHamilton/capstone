package com.teksystems.hamilton.austin.capstone.database.dao;

import com.teksystems.hamilton.austin.capstone.entity.UserFollowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowingDAO extends JpaRepository<UserFollowing, Long> {
    public List<UserFollowing> findByFollowingUser_Id(@Param("followingUser") Long followingUser);
}
