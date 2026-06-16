package com.server.app.repositories.finance;

import com.server.app.entities.finance.EstadoPlanPago;
import com.server.app.entities.finance.PlanPago;
import com.server.app.entities.finance.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanPagoRepository extends JpaRepository<PlanPago, Integer> {

    List<PlanPago> findByPrestamoOrderByNumeroCuotaAsc(Prestamo prestamo);

    List<PlanPago> findByPrestamoAndEstadoOrderByNumeroCuotaAsc(Prestamo prestamo, EstadoPlanPago estado);
}
