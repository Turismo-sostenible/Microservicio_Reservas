package com.unicauca.reservas.application.usecases;

import com.unicauca.reservas.application.port.input.UpdateReservaUseCase;
import com.unicauca.reservas.application.port.output.ReservaRepositoryPort;
import com.unicauca.reservas.domain.models.Reserva;
import com.unicauca.reservas.domain.models.ReservaId;

import java.util.Optional;

public class UpdateReservaUseCaseImpl implements UpdateReservaUseCase {

    private final ReservaRepositoryPort reservaRepositoryPort;

    public UpdateReservaUseCaseImpl(ReservaRepositoryPort reservaRepositoryPort) {
        this.reservaRepositoryPort = reservaRepositoryPort;
    }

    @Override
    public boolean updateReserva(ReservaRequest reserva) {
        Optional<Reserva> existingReserva = reservaRepositoryPort.findById(reserva.id());

        if (existingReserva.isPresent()) {
            return false;
        }

        Reserva nuevaReserva = existingReserva.get();

        Reserva reservaActualizada = new Reserva(
                reserva.id(),
                reserva.usuario(),
                reserva.guia(),
                reserva.plan(),
                reserva.participantes(),
                reserva.refrigerio(),
                reserva.fechaReserva(),
                reserva.precioTotal()
        );

        nuevaReserva.actualizarReserva(reservaActualizada);
        reservaRepositoryPort.update(nuevaReserva);

        return true;
    }
}
