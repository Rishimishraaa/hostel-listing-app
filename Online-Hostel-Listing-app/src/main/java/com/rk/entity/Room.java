package com.rk.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String roomNumber; // e.g., "101", "102"
    private int floorNumber;
    private int capacity; // total allowed occupants
    private int occupacy;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "floor_id")
    private Floor floor;
    
    
    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;
   

    @ToString.Exclude
    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Booking> bookings=new ArrayList<>();
}
