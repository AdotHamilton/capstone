package com.teksystems.capstone.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="creator_id")
    private User creator;
    @Column(name="image_url")
    private String image;
    @Column(name="content")
    private String content;
    @Column(name="create_date")
    private Date date = new Date();
}
