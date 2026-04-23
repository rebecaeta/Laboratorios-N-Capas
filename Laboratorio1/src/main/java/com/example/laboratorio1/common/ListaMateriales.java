package com.example.laboratorio1.common;

import com.example.laboratorio1.domain.model.Material;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class ListaMateriales {

    private final List<Material> materiales;

    public ListaMateriales() {
        this.materiales = new ArrayList<>();

        materiales.add(Material.builder()
                .id(1L)
                .nombre("Ámbar Rojo")
                .categoria("Mineral")
                .efecto("Defensa")
                .ubicacion("Cordillera de Hebra")
                .precio(30)
                .rareza("Común")
                .build());

        materiales.add(Material.builder()
                .id(2L)
                .nombre("Ala de Keese")
                .categoria("Parte de Monstruo")
                .efecto("Sigilo")
                .ubicacion("Campos de Hyrule")
                .precio(15)
                .rareza("Poco Común")
                .build());

        materiales.add(Material.builder()
                .id(3L)
                .nombre("Pimienta Ardiente")
                .categoria("Planta")
                .efecto("Ataque")
                .ubicacion("Volcán de Eldin")
                .precio(10)
                .rareza("Común")
                .build());

        materiales.add(Material.builder()
                .id(4L)
                .nombre("Diamante")
                .categoria("Mineral")
                .efecto("Defensa")
                .ubicacion("Montaña de la Muerte")
                .precio(500)
                .rareza("Legendario")
                .build());
    }

}