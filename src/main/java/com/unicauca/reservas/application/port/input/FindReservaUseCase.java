package com.unicauca.reservas.application.port.input;

import com.unicauca.reservas.domain.models.Reserva;
import com.unicauca.reservas.domain.models.ReservaId;

import java.util.List;
import java.util.Optional;

public interface FindReservaUseCase {
    Optional<Reserva> findById(ReservaId id);
    List<Reserva> findAll();
}
