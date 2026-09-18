package com.RoomMateX.entity;

import com.RoomMateX.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    @Column(unique = true)
    private String email;

    private String phone;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String gender;
    private int age;
    private String occupation;
    private int budget;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

    private Boolean enabled;

    @JsonIgnore
    @OneToOne(mappedBy = "user",  cascade = CascadeType.ALL, orphanRemoval = true)
    private Preference preference;

}
