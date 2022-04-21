package com.teksystems.capstone.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;
import com.teksystems.capstone.entity.Post;
import org.hibernate.validator.constraints.Length;

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
    private String displayName;

    @Column(name="zip_code")
    private String zipCode;

    @Column(name="state")
    @Size(min=2, max=2, message = "State Must be a valid abbreviation")
    private String state;

    @Column(name="business_address")
    private String businessAddress;

    @Column(name="password", nullable = false)
    @Size(min=6, message = "Password must be greater than 6 characters!")
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
