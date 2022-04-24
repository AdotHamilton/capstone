package com.teksystems.hamilton.austin.capstone.entity;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name="vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="vin", nullable = true)
    private String VIN;
    @Range(min = 1950, max = 2022, message = "Vehicle year must be between 1950-2022")
    private int year;
    private String make;
    private String model;
}
