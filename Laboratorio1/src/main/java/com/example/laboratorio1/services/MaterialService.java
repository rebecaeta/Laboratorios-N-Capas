package com.example.laboratorio1.services;

import com.example.laboratorio1.domain.model.Material;
import com.example.laboratorio1.repositories.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository repository;

    public List<Material> precioMayorAMenor(){
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Material::getPrecio).reversed())
                .toList();
    }

    public Material materialMayorPrecio(){
        return repository.findAll()
                .stream()
                .max(Comparator.comparing(Material::getPrecio))
                .orElse(null);
    }

    public List<Material> obtenerRareza(){
        return repository.findAll()
                .stream()
                .filter(material -> material.getRareza().equalsIgnoreCase("Legendario"))
                .toList();
    }

    public List<String> ubicacionesUnicas(){
        return repository.findAll()
                .stream()
                .map(Material::getUbicacion)
                .distinct()
                .toList();
    }

    public List<Material> getAll(){
        return repository.findAll();
    }


}
