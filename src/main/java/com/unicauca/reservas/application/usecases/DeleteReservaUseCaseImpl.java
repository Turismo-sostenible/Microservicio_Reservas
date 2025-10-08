package com.unicauca.reservas.application.usecases;

import com.unicauca.reservas.application.port.input.DeleteReservaUseCase;
import com.unicauca.reservas.application.port.output.ReservaRepositoryPort;
import com.unicauca.reservas.domain.models.ReservaId;

public class DeleteReservaUseCaseImpl implements DeleteReservaUseCase {

    private final ReservaRepositoryPort reservaRepositoryPort;

    public DeleteReservaUseCaseImpl(ReservaRepositoryPort reservaRepositoryPort) {
        this.reservaRepositoryPort = reservaRepositoryPort;
    }

    @Override
    public void deleteReserva(ReservaId id) {
        reservaRepositoryPort.deleteById(id);
    }
}
