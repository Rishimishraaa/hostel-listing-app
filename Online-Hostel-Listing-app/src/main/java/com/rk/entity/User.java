package com.rk.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@SQLRestriction("is_active = true")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String phone;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // -----------------------------------
    // Existing relations
    // -----------------------------------
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Favorite> favorites = new ArrayList<>();

    @ToString.Exclude
    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY)
    private Booking bookings;

    @ToString.Exclude
    @OneToOne(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Hostel hostels;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rating> ratingsGiven = new ArrayList<>();

    // -----------------------------------
    // Hostel / Student specific fields
    // -----------------------------------

    
    
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room; // Assigned room

    private String paymentStatus; // PAID / PENDING
    private LocalDate joiningDate;
    private LocalDateTime lastPaymentDate;
    
    @Column(name = "is_active")
    private Boolean isActive = true; // default true
}
