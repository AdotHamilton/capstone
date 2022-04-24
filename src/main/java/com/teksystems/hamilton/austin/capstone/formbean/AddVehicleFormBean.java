package com.teksystems.hamilton.austin.capstone.formbean;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddVehicleFormBean {
    private Long user_id;
    private String make;
    private String model;
    private int year;
}
