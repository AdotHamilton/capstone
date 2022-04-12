package com.teksystems.capstone.entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {
    private String email;
    private String password;
}
