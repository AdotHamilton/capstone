package com.teksystems.hamilton.austin.capstone.formbean;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventFormBean {

    private int Id;
    @Size(min = 4, max=30, message = "Title must be 4-30 characters")
    private String title;
    @Size(max=200, message = "Description must be less than 200 characters")
    private String description;
    @NotBlank(message = "Must include an address")
    private String address;

    private MultipartFile file;
    @NotBlank(message = "Must include state")
    private String state;

    private Long userId;

    private String date;
}
