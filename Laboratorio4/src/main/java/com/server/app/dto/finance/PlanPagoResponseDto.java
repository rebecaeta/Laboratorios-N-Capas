package com.server.app.dto.finance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.app.entities.finance.EstadoPlanPago;
import com.server.app.entities.finance.PlanPago;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PlanPagoResponseDto {
    private Integer id;
    private Integer numeroCuota;
    private BigDecimal montoCapital;
    private BigDecimal montoInteres;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVencimiento;
    private EstadoPlanPago estado;

    public PlanPagoResponseDto(PlanPago planPago) {
        this.id = planPago.getId();
        this.numeroCuota = planPago.getNumeroCuota();
        this.montoCapital = planPago.getMontoCapital();
        this.montoInteres = planPago.getMontoInteres();
        this.fechaVencimiento = planPago.getFechaVencimiento();
        this.estado = planPago.getEstado();
    }
}
