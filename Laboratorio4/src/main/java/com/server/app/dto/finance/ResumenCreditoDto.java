package com.server.app.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ResumenCreditoDto {
    private BigDecimal totalSolicitado;
    private BigDecimal totalPendiente;
    private BigDecimal totalPagado;
    private Long prestamosActivos;
    private Long prestamosPagados;
}
