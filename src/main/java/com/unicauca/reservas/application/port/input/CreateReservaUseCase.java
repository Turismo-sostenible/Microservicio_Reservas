package com.unicauca.reservas.application.port.input;

import com.unicauca.reservas.domain.models.*;

import java.time.LocalDateTime;

public interface CreateReservaUseCase {
    Reserva createReserva(ReservaRequest reserva);

    record ReservaRequest(Plan plan, Usuario usuario, Guia guia, Integer participantes, Refrigerio refrigerio, LocalDateTime fechaReserva, Integer precioTotal) { }
}
