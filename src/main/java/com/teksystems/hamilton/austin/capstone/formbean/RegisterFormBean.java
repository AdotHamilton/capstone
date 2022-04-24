package com.teksystems.hamilton.austin.capstone.formbean;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFormBean {
    private String email;
    private String displayName;
    private String password;
    private String confirmPw;
    private String zipCode;
    private String state;
    private String businessAddress;
    private Boolean isBusiness;

}
