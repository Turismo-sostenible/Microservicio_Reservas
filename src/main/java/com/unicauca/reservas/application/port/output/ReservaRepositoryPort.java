package com.unicauca.reservas.application.port.output;

import com.unicauca.reservas.domain.models.Reserva;
import com.unicauca.reservas.domain.models.ReservaId;

import java.util.List;
import java.util.Optional;

public interface ReservaRepositoryPort {
    Reserva save(Reserva reserva);
    void deleteById(ReservaId id);
    Optional<Reserva> findById(ReservaId id);
    List<Reserva> findAll();
    boolean update(Reserva reserva);

}
