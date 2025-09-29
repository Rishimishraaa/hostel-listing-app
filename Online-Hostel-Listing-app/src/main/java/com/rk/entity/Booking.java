package com.rk.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "bookings")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDate = LocalDateTime.now();
    private LocalDateTime endDate;

    private String status; // PENDING/CONFIRM/CANCELLED
    private Double totalAmount;

  
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "hostel_id")
    private Hostel hostel;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    // -----------------------------------
    // Payments relation
    // -----------------------------------
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Payment> payments = new ArrayList<>();
    
    private Boolean isActive = true; // default true

}
