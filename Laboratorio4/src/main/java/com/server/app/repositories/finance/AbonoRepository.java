package com.server.app.repositories.finance;

import com.server.app.entities.finance.Abono;
import com.server.app.entities.finance.PlanPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbonoRepository extends JpaRepository<Abono, Integer> {
    List<Abono> findByPlanPago(PlanPago planPago);
}
