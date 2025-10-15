package com.unicauca.reservas.application.usecases;

import com.unicauca.reservas.application.port.input.CreateReservaUseCase;
import com.unicauca.reservas.application.port.output.ReservaRepositoryPort;
import com.unicauca.reservas.domain.models.*;

public class CreateReservaUseCaseImpl implements CreateReservaUseCase {

    private final ReservaRepositoryPort reservaRepositoryPort;

    public CreateReservaUseCaseImpl(ReservaRepositoryPort reservaRepositoryPort) {
        this.reservaRepositoryPort = reservaRepositoryPort;
    }

    @Override
    public Reserva createReserva(ReservaRequest reserva) {
        Reserva nuevaReserva = Reserva.create(
                new Usuario(reserva.usuario(), null, null, null),
                new Guia(reserva.guia(), null),
                new Plan(reserva.plan(), null, null, null, null, null),
                reserva.participantes(),
                Refrigerio.valueOf(reserva.refrigerio()),
                reserva.fechaReserva(),
                reserva.precioTotal()
        );

        return reservaRepositoryPort.save(nuevaReserva);
    }
}
