package com.example.laboratorio2.controller;

import com.example.laboratorio2.domain.entity.Pirate;
import com.example.laboratorio2.service.PirateService;
import com.example.laboratorio2.service.impl.PirateServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pirates")
@AllArgsConstructor
public class PirateController {
    private final PirateService pirateService;

    @PostMapping
    public ResponseEntity<Pirate> createPirate(@RequestBody Pirate pirate) {
        Pirate created = pirateService.createPirate(pirate);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Pirate>> getAllPirates() {
        return ResponseEntity.status(HttpStatus.OK).body(pirateService.getAllPirates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pirate> getPirateById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(pirateService.getPirateById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pirate> updatePirate(@PathVariable UUID id, @RequestBody Pirate pirate) {
        return ResponseEntity.status(HttpStatus.OK).body(pirateService.updatePirate(id, pirate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePirate(@PathVariable UUID id) {
        pirateService.deletePirate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
