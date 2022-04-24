package com.teksystems.hamilton.austin.capstone.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name="event")
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="title")
    private String title;
    @Column(name="address")
    private String address;
    @Column(name="description")
    private String description;
    @Column(name="banner_url")
    private String bannerUrl;
    @Column(name="state")
    private String state;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User creator;
    @Column(name="meeting_time")
    private Date date;
    @Column(name="created_at")
    private Date created = new Date();

}
