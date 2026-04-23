package com.example.laboratorio1;

import com.example.laboratorio1.domain.model.Material;
import com.example.laboratorio1.services.MaterialService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Laboratorio1Application {

    public static void main(String[] args) {
        SpringApplication.run(Laboratorio1Application.class, args);
    }

    @Bean
    CommandLineRunner run(MaterialService materialService) {
        return args -> {
            System.out.println("Caso de Hyrule");

            System.out.println("==== Catalogo Completo =====");
            for (Material m : materialService.getAll()) {
                System.out.println("[HYRULE-DB] Nombre: " + m.getNombre()
                        + " | Categoría: " + m.getCategoria()
                        + " | Precio: " + m.getPrecio() + " Rupias");
            }
            System.out.println("==== Materiales ordenados por precio (mayor a menor) =====");
            materialService.precioMayorAMenor().forEach(m ->
                    System.out.println("[HYRULE-DB] Nombre: " + m.getNombre()
                            + " | Categoría: " + m.getCategoria()
                            + " | Precio: " + m.getPrecio() + " Rupias")
            );
            System.out.println("==== Material más caro =====");
            Material masCaro = materialService.materialMayorPrecio();
                System.out.println("[HYRULE-DB] Nombre: " + masCaro.getNombre()
                    + " | Categoría: " + masCaro.getCategoria()
                    + " | Precio: " + masCaro.getPrecio() + " Rupias");

            System.out.println("==== Material Legendario=====");
            materialService.obtenerRareza().forEach(material ->
                    System.out.println("[HYRULE-DB] Nombre: " + material.getNombre()
                            + " | Categoría: " + material.getCategoria()
                            + " | Precio: " + material.getPrecio() + " Rupias")
            );

            System.out.println("==== Ubicaciones únicas =====");
            materialService.ubicacionesUnicas().forEach(ubicacion ->
                    System.out.println("[HYRULE-DB] Ubicación: " + ubicacion)
            );
        };
    }
}
