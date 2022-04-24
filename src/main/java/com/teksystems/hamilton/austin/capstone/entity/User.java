package com.teksystems.hamilton.austin.capstone.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email", nullable = false, unique = true)
    @Email(message = "Please provide a valid email")
    private String email;

    @Column(name="display_name", nullable = false, unique = true)
    @Size(min=3, max=15, message = "Display Name must be 3-15 characters")
    private String displayName;

    @Column(name="zip_code")
    @Size(max=9, message = "Postal code must be valid")
    private String zipCode;

    @Column(name="state")
    @Size(min=0, max=2, message = "State Must be a valid abbreviation")
    private String state;

    @Column(name="business_address")
    private String businessAddress;
    @Column(name="phone", nullable = true)
    @Size( min=7,max=11, message = "Phone number must be valid")
    private String phone;

    @Column(name="password", nullable = false)
    private String password;

    @Transient
    private String confirmPw;

    @Column(name="pfp", nullable = true, length = 200)
    private String pfp;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_vehicles",
            joinColumns = @JoinColumn(name= "owner_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id")
    )
    private List<Vehicle> vehicles;

    @Column(name="create_date", updatable = false)
    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    protected void onCreate(){ this.createdAt = new Date(); }
    @PreUpdate
    protected void onUpdate(){ this.updatedAt = new Date(); }

}
