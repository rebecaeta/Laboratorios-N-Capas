package com.server.app.services.finance;

import com.server.app.dto.finance.*;
import com.server.app.entities.User;
import com.server.app.entities.finance.*;
import com.server.app.repositories.finance.AbonoRepository;
import com.server.app.repositories.finance.PlanPagoRepository;
import com.server.app.repositories.finance.PrestamoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final PlanPagoRepository planPagoRepository;
    private final AbonoRepository abonoRepository;

    public PrestamoService(
            PrestamoRepository prestamoRepository,
            PlanPagoRepository planPagoRepository,
            AbonoRepository abonoRepository
    ) {
        this.prestamoRepository = prestamoRepository;
        this.planPagoRepository = planPagoRepository;
        this.abonoRepository = abonoRepository;
    }

    public Page<PrestamoResponseDto> listarPrestamos(User usuario, Pageable pageable) {
        return prestamoRepository.findByUsuario(usuario, pageable)
                .map(PrestamoResponseDto::new);
    }

    @Transactional
    public PrestamoResponseDto crearPrestamo(User usuario, PrestamoCreateDto dto) {

        Prestamo prestamo = new Prestamo();
        prestamo.setCapitalSolicitado(dto.getCapitalSolicitado());
        prestamo.setTasaInteresAnual(dto.getTasaInteresAnual());
        prestamo.setPlazoMeses(dto.getPlazoMeses());
        prestamo.setEstado(EstadoPrestamo.APROBADO);
        prestamo.setUsuario(usuario);

        generarPlanPagos(prestamo);

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);

        return new PrestamoResponseDto(prestamoGuardado);
    }

    public List<PlanPagoResponseDto> listarPlanesPago(User usuario, Integer prestamoId) {

        Prestamo prestamo = obtenerPrestamoDelUsuario(usuario, prestamoId);

        return planPagoRepository.findByPrestamoOrderByNumeroCuotaAsc(prestamo)
                .stream()
                .map(PlanPagoResponseDto::new)
                .toList();
    }

    @Transactional
    public AbonoResponseDto registrarAbono(User usuario, AbonoCreateDto dto) {

        PlanPago planPago = planPagoRepository.findById(dto.getPlanPagoId())
                .orElseThrow(() -> new RuntimeException("Plan de pago no encontrado"));

        validarPropietario(usuario, planPago.getPrestamo());

        if (planPago.getEstado() == EstadoPlanPago.PAGADO) {
            throw new RuntimeException("Esta cuota ya fue pagada");
        }

        BigDecimal recargoMora = calcularRecargoMora(planPago);

        Abono abono = new Abono();
        abono.setMonto(dto.getMonto());
        abono.setFechaPago(LocalDate.now());
        abono.setRecargoMora(recargoMora);
        abono.setPlanPago(planPago);

        BigDecimal totalCuota = planPago.getMontoCapital()
                .add(planPago.getMontoInteres())
                .add(recargoMora);

        if (dto.getMonto().compareTo(totalCuota) >= 0) {
            planPago.setEstado(EstadoPlanPago.PAGADO);
            planPagoRepository.save(planPago);
        }

        Abono abonoGuardado = abonoRepository.save(abono);

        actualizarEstadoPrestamoSiCorresponde(planPago.getPrestamo());

        return new AbonoResponseDto(abonoGuardado);
    }

    public ResumenCreditoDto obtenerResumenCredito(User usuario) {

        List<Prestamo> prestamos = prestamoRepository.findByUsuario(usuario);

        BigDecimal totalSolicitado = BigDecimal.ZERO;
        BigDecimal totalPendiente = BigDecimal.ZERO;
        BigDecimal totalPagado = BigDecimal.ZERO;

        long prestamosActivos = 0;
        long prestamosPagados = 0;

        for (Prestamo prestamo : prestamos) {

            totalSolicitado = totalSolicitado.add(prestamo.getCapitalSolicitado());

            if (prestamo.getEstado() == EstadoPrestamo.PAGADO) {
                prestamosPagados++;
            } else {
                prestamosActivos++;
            }

            List<PlanPago> planes = planPagoRepository.findByPrestamoOrderByNumeroCuotaAsc(prestamo);

            for (PlanPago plan : planes) {
                BigDecimal montoCuota = plan.getMontoCapital().add(plan.getMontoInteres());

                if (plan.getEstado() == EstadoPlanPago.PAGADO) {
                    totalPagado = totalPagado.add(montoCuota);
                } else {
                    totalPendiente = totalPendiente.add(montoCuota);
                }
            }
        }

        return new ResumenCreditoDto(
                totalSolicitado,
                totalPendiente,
                totalPagado,
                prestamosActivos,
                prestamosPagados
        );
    }

    private void generarPlanPagos(Prestamo prestamo) {

        BigDecimal capital = prestamo.getCapitalSolicitado();
        BigDecimal tasaAnual = prestamo.getTasaInteresAnual();
        int plazoMeses = prestamo.getPlazoMeses();

        BigDecimal tasaMensual = tasaAnual
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal cuotaMensual = calcularCuotaMensual(capital, tasaMensual, plazoMeses);

        BigDecimal saldoPendiente = capital;

        for (int i = 1; i <= plazoMeses; i++) {

            BigDecimal interes = saldoPendiente
                    .multiply(tasaMensual)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal montoCapital = cuotaMensual
                    .subtract(interes)
                    .setScale(2, RoundingMode.HALF_UP);

            if (i == plazoMeses) {
                montoCapital = saldoPendiente.setScale(2, RoundingMode.HALF_UP);
            }

            PlanPago planPago = new PlanPago();
            planPago.setNumeroCuota(i);
            planPago.setMontoCapital(montoCapital);
            planPago.setMontoInteres(interes);
            planPago.setFechaVencimiento(LocalDate.now().plusMonths(i));
            planPago.setEstado(EstadoPlanPago.PENDIENTE);
            planPago.setPrestamo(prestamo);

            prestamo.getPlanesPago().add(planPago);

            saldoPendiente = saldoPendiente.subtract(montoCapital);
        }
    }

    private BigDecimal calcularCuotaMensual(BigDecimal capital, BigDecimal tasaMensual, int plazoMeses) {

        if (tasaMensual.compareTo(BigDecimal.ZERO) == 0) {
            return capital
                    .divide(BigDecimal.valueOf(plazoMeses), 2, RoundingMode.HALF_UP);
        }

        double p = capital.doubleValue();
        double i = tasaMensual.doubleValue();
        double n = plazoMeses;

        double cuota = p * i / (1 - Math.pow(1 + i, -n));

        return BigDecimal.valueOf(cuota).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularRecargoMora(PlanPago planPago) {

        LocalDate hoy = LocalDate.now();

        if (!hoy.isAfter(planPago.getFechaVencimiento())) {
            return BigDecimal.ZERO;
        }

        BigDecimal montoCuota = planPago.getMontoCapital().add(planPago.getMontoInteres());

        return montoCuota
                .multiply(BigDecimal.valueOf(0.02))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private Prestamo obtenerPrestamoDelUsuario(User usuario, Integer prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        validarPropietario(usuario, prestamo);

        return prestamo;
    }

    private void validarPropietario(User usuario, Prestamo prestamo) {
        if (prestamo.getUsuario().getId() != usuario.getId()) {
            throw new RuntimeException("No tienes permiso para acceder a este préstamo");
        }
    }

    private void actualizarEstadoPrestamoSiCorresponde(Prestamo prestamo) {

        List<PlanPago> pendientes = planPagoRepository
                .findByPrestamoAndEstadoOrderByNumeroCuotaAsc(prestamo, EstadoPlanPago.PENDIENTE);

        if (pendientes.isEmpty()) {
            prestamo.setEstado(EstadoPrestamo.PAGADO);
            prestamoRepository.save(prestamo);
        }
    }
}
