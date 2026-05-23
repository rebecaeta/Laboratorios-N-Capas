package com.example.laboratorio3.controllers;

import com.example.laboratorio3.domain.dto.request.CreateSpecimenRequest;
import com.example.laboratorio3.domain.dto.request.UpdateSpecimenRequest;
import com.example.laboratorio3.domain.dto.response.GeneralResponse;
import com.example.laboratorio3.services.SpecimenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/specimen")
public class SpecimenController {
    private final SpecimenService specimenService;

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse> createSpecimen(@RequestBody CreateSpecimenRequest request){
        return buildResponse("Specimen created successfully",
                HttpStatus.CREATED, specimenService.createSpecimen(request));
    }

    @GetMapping("/getBy/{id}")
    public ResponseEntity<GeneralResponse> getSpecimenById(@PathVariable UUID id) {
        return buildResponse("Specimen found",
                HttpStatus.OK, specimenService.getSpecimenById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GeneralResponse> updateSpecimen(@PathVariable UUID id, @RequestBody UpdateSpecimenRequest request) {
        return buildResponse("Specimen updated successfully",
                HttpStatus.OK, specimenService.updateSpecimen(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GeneralResponse> deleteSpecimen(@PathVariable UUID id) {
        return buildResponse("Specimen deleted successfully",
                HttpStatus.OK, specimenService.deleteSpecimen(id));
    }

    public ResponseEntity<GeneralResponse> buildResponse(String message, HttpStatus status, Object data) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
        return ResponseEntity
                .status(status)
                .body(GeneralResponse.builder().uri(uri).time(LocalDateTime.now())
                .message(message)
                .status(status.value())
                .data(data)
                .build());
    }
}