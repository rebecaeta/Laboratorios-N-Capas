package com.example.laboratorio2.repository;

import com.example.laboratorio2.domain.entity.Pirate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PirateRepository extends JpaRepository<Pirate, UUID> {
}
