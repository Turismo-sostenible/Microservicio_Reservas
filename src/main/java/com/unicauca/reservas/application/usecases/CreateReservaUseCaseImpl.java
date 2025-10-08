package com.unicauca.reservas.application.usecases;

import com.unicauca.reservas.application.port.input.CreateReservaUseCase;
import com.unicauca.reservas.application.port.output.ReservaRepositoryPort;
import com.unicauca.reservas.domain.models.Reserva;

public class CreateReservaUseCaseImpl implements CreateReservaUseCase {

    private final ReservaRepositoryPort reservaRepositoryPort;

    public CreateReservaUseCaseImpl(ReservaRepositoryPort reservaRepositoryPort) {
        this.reservaRepositoryPort = reservaRepositoryPort;
    }

    @Override
    public Reserva createReserva(ReservaRequest reserva) {
        Reserva nuevaReserva = Reserva.create(
                reserva.usuario(),
                reserva.guia(),
                reserva.plan(),
                reserva.participantes(),
                reserva.refrigerio(),
                reserva.fechaReserva(),
                reserva.precioTotal()
        );

        return reservaRepositoryPort.save(nuevaReserva);
    }
}
