package com.server.app.repositories.finance;

import com.server.app.entities.User;
import com.server.app.entities.finance.EstadoPrestamo;
import com.server.app.entities.finance.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    Page<Prestamo> findByUsuario(User usuario, Pageable pageable);

    List<Prestamo> findByUsuario(User usuario);
}
