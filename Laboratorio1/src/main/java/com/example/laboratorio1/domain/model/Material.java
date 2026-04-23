package com.example.laboratorio1.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Material {
    private Long id;
    private String nombre;
    private String categoria;
    private String efecto;
    private String ubicacion;
    private double precio;
    private String rareza;
}
