package com.unicauca.reservas.application.usecases;

import com.unicauca.reservas.application.port.input.FindReservaUseCase;
import com.unicauca.reservas.application.port.output.ReservaRepositoryPort;
import com.unicauca.reservas.domain.models.Reserva;
import com.unicauca.reservas.domain.models.ReservaId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FindReservaUseCaseImpl implements FindReservaUseCase {

    private final ReservaRepositoryPort reservaRepositoryPort;

    public FindReservaUseCaseImpl(ReservaRepositoryPort reservaRepositoryPort) {
        this.reservaRepositoryPort = reservaRepositoryPort;
    }

    @Override
    public Optional<Reserva> findById(ReservaId id) {
        return reservaRepositoryPort.findById(id);
    }

    @Override
    public List<Reserva> findAll() {
        return reservaRepositoryPort.findAll();
    }
}
