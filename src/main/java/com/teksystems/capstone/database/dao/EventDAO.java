package com.teksystems.capstone.database.dao;


import com.teksystems.capstone.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventDAO extends JpaRepository<Event, Long> {
    public List<Event> findByState(@Param("state") String state);
    public Event findEventById(@Param("id") Long id);
}
