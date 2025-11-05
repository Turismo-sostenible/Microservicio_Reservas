package com.unicauca.reservas.application.port.input;

import com.unicauca.reservas.domain.models.*;

import java.time.LocalDateTime;

public interface CreateReservaUseCase {
    Reserva createReserva(ReservaRequest reserva);

    record ReservaRequest(String plan, String usuario, String guia, Integer participantes, String refrigerio, LocalDateTime fechaReserva, Integer precioTotal) { }
}
