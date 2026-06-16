package com.server.app.controllers;

import com.server.app.dto.finance.*;
import com.server.app.dto.response.Pagination;
import com.server.app.dto.response.PaginationMeta;
import com.server.app.entities.User;
import com.server.app.services.finance.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finanzas")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping("/prestamos")
    public ResponseEntity<Pagination<PrestamoResponseDto>> listarPrestamos(
            @AuthenticationPrincipal User usuario,
            Pageable pageable
    ) {
        Page<PrestamoResponseDto> prestamos = prestamoService.listarPrestamos(usuario, pageable);

        PaginationMeta meta = new PaginationMeta(
                prestamos.getNumber(),
                prestamos.getSize(),
                prestamos.getTotalPages(),
                prestamos.getTotalElements()
        );

        Pagination<PrestamoResponseDto> response = new Pagination<>(
                prestamos.getContent(),
                meta
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/prestamos")
    public ResponseEntity<PrestamoResponseDto> crearPrestamo(
            @AuthenticationPrincipal User usuario,
            @RequestBody @Valid PrestamoCreateDto dto
    ) {
        return ResponseEntity.ok(prestamoService.crearPrestamo(usuario, dto));
    }

    @GetMapping("/prestamos/{id}/planes-pago")
    public ResponseEntity<List<PlanPagoResponseDto>> listarPlanesPago(
            @AuthenticationPrincipal User usuario,
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(prestamoService.listarPlanesPago(usuario, id));
    }

    @PostMapping("/abonos")
    public ResponseEntity<AbonoResponseDto> registrarAbono(
            @AuthenticationPrincipal User usuario,
            @RequestBody @Valid AbonoCreateDto dto
    ) {
        return ResponseEntity.ok(prestamoService.registrarAbono(usuario, dto));
    }

    @GetMapping("/resumen-credito")
    public ResponseEntity<ResumenCreditoDto> obtenerResumenCredito(
            @AuthenticationPrincipal User usuario
    ) {
        return ResponseEntity.ok(prestamoService.obtenerResumenCredito(usuario));
    }
}
