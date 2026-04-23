package com.example.laboratorio1.repositories;

import com.example.laboratorio1.common.ListaMateriales;
import com.example.laboratorio1.domain.model.Material;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MaterialRepository {

    private final ListaMateriales listaMateriales;

    public List<Material> findAll() {
        return listaMateriales.getMateriales();
    }
}
