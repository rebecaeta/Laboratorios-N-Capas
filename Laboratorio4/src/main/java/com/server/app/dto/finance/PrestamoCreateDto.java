package com.server.app.dto.finance;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PrestamoCreateDto {
    @NotNull(message = "El capital solicitado es obligatorio")
    @DecimalMin(value = "0.01", message = "El capital solicitado debe ser mayor que cero")
    private BigDecimal capitalSolicitado;

    @NotNull(message = "La tasa de interés anual es obligatoria")
    @DecimalMin(value = "0.01", message = "La tasa de interés anual debe ser mayor que cero")
    private BigDecimal tasaInteresAnual;

    @NotNull(message = "El plazo en meses es obligatorio")
    @Min(value = 1, message = "El plazo debe ser de al menos 1 mes")
    private Integer plazoMeses;
}
