package com.unicauca.reservas.application.port.input;

import com.unicauca.reservas.domain.models.*;

import java.time.LocalDateTime;

public interface CreateReservaUseCase {
    Reserva createReserva(ReservaRequest reserva);

    record ReservaRequest(Integer plan, Integer usuario, Integer guia, Integer participantes, String refrigerio, LocalDateTime fechaReserva, Integer precioTotal) { }
}
