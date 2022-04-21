package com.teksystems.capstone.formbean;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventFormBean {

    private int Id;

    private String title;

    private String description;

    private String address;

    private MultipartFile file;

    private String state;

    private Long userId;

    private String date;
}
