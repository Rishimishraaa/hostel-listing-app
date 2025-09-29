package com.rk.entity;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room_type_images")
public class RoomTypeImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String imageUrl;
	
	@JsonIgnore
	@ToString.Exclude
//	@JsonBackReference(value = "rooms_img")
	@ManyToOne
	@JoinColumn(name = "room_type_id")
	private RoomType roomType;
}
