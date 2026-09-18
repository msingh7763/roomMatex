package com.RoomMateX.entity;


import com.RoomMateX.enums.RoomStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Table(name="rooms", indexes = @Index(columnList = "city"))
@Builder

public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Title required")
    private String title;

    @Column(length = 1000)
    private String description;

    private Double price;

    private Integer rent;

    private String city;

    private Double latitude;
    private Double longitude;

    private Boolean available = true;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner_id")
    @JsonIgnoreProperties({"rooms","preference"})
    private User owner;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime created_at;

    @OneToMany(mappedBy = "room")
    private List<BookingRequest> bookingRequests;

}
