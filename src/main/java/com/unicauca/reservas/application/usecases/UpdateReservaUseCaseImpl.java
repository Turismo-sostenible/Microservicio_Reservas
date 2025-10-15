package com.unicauca.reservas.application.usecases;

import com.unicauca.reservas.application.port.input.UpdateReservaUseCase;
import com.unicauca.reservas.application.port.output.ReservaRepositoryPort;
import com.unicauca.reservas.domain.models.Reserva;

import java.util.Optional;

public class UpdateReservaUseCaseImpl implements UpdateReservaUseCase {

    private final ReservaRepositoryPort reservaRepositoryPort;

    public UpdateReservaUseCaseImpl(ReservaRepositoryPort reservaRepositoryPort) {
        this.reservaRepositoryPort = reservaRepositoryPort;
    }

    @Override
    public boolean updateReserva(ReservaRequest reserva) {
        Optional<Reserva> existingReserva = reservaRepositoryPort.findById(reserva.id());

        if (existingReserva.isEmpty()) {
            return false;
        }

        Reserva nuevaReserva = existingReserva.get();

        Reserva reservaActualizada = new Reserva(
                nuevaReserva.getId(),
                nuevaReserva.getUsuario(),
                nuevaReserva.getGuia(),
                nuevaReserva.getPlan(),
                nuevaReserva.getParticipantes(),
                reserva.refrigerio(),
                reserva.fechaReserva(),
                nuevaReserva.getPrecioTotal()
        );

        nuevaReserva.actualizarReserva(reservaActualizada);
        reservaRepositoryPort.update(nuevaReserva);
        return true;
    }
}
