package com.example.laboratorio2.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "pirate")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pirate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "pirateName")
    private String name;

    @Column(name = "pirateBounty")
    private double bounty;

    @Column(name = "pirateCrew")
    private String crew;

    @Column(name = "pirateAlive")
    private Boolean isAlive;
}
