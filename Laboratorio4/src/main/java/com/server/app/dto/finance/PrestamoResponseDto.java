package com.server.app.dto.finance;

import com.server.app.entities.finance.EstadoPrestamo;
import com.server.app.entities.finance.Prestamo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PrestamoResponseDto {
    private Integer id;
    private BigDecimal capitalSolicitado;
    private BigDecimal tasaInteresAnual;
    private Integer plazoMeses;
    private EstadoPrestamo estado;
    private Integer usuarioId;
    private List<PlanPagoResponseDto> planesPago;

    public PrestamoResponseDto(Prestamo prestamo) {
        this.id = prestamo.getId();
        this.capitalSolicitado = prestamo.getCapitalSolicitado();
        this.tasaInteresAnual = prestamo.getTasaInteresAnual();
        this.plazoMeses = prestamo.getPlazoMeses();
        this.estado = prestamo.getEstado();
        this.usuarioId = prestamo.getUsuario().getId();

        if (prestamo.getPlanesPago() != null) {
            this.planesPago = prestamo.getPlanesPago()
                    .stream()
                    .map(PlanPagoResponseDto::new)
                    .toList();
        }
    }
}
