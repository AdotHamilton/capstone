package com.teksystems.hamilton.austin.capstone.formbean;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceFormBean {
    private Long id;
    @Size(min=2, max = 24, message = "Title must be 2-24 characters")
    private String title;
    @Size(max=200, message = "Description must be 200 characters or less")
    private String description;
    private MultipartFile file;
    @NotBlank
    private Long user_id;

}
