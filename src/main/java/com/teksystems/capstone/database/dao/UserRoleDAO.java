package com.teksystems.capstone.database.dao;

import com.teksystems.capstone.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface UserRoleDAO extends JpaRepository<UserRole, Long> {
    @Query(value = "Select u.*, ur.role as role FROM users u, user_roles ur WHERE u.id = ur.user_id", nativeQuery = true)
    public List<Map<String, Object>> findUsersAndRoles();

    @Query(value="Select u.*, ur.role as role FROM users u, user_roles ur WHERE u.id = ur.user_id AND u.id = :userId", nativeQuery = true)
    public List<Map<String, Object>> findUserRoleById(@Param("userId") Long id);
}
