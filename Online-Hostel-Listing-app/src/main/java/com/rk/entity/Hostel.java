package com.rk.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "hostels")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hostel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private boolean status;
    private String description;

    @Embedded
    private Address address;

    @Embedded
    private ContactInformation contactInfo;

    @Embedded
    private Facilities facilities;

    @Embedded
    private Policies policies;

    @Embedded
    private FoodInfo foodInfo;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rating> ratings = new ArrayList<>();

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

 
    
    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL)
    private List<RoomType> roomType;
    
    
 
    @ToString.Exclude
    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Floor> floors = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HostelImage> images = new ArrayList<>();
    
    
    @Override
    public int hashCode() {
        return Objects.hash(id); // sirf primary key
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Hostel)) return false;
        Hostel other = (Hostel) obj;
        return Objects.equals(this.id, other.id);
    }

}
