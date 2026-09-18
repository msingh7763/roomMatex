package com.RoomMateX.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="preferences")
@Builder

public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean smoking;

    private boolean drinking;

    @Column(name="budget_min")
    private Integer budgetMin;

    @Column(name="budget_max")
    private Integer budgetMax;

    @Column(name="gender_preference")
    private String genderPreference;

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "user_id", unique = true)
    private User user;

}
