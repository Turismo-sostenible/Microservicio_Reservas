package com.unicauca.reservas.application.port.input;

import com.unicauca.reservas.domain.models.*;

import java.time.LocalDateTime;

public interface UpdateReservaUseCase {
    boolean updateReserva(ReservaRequest reserva);

    record ReservaRequest(ReservaId id, Plan plan, Usuario usuario, Guia guia, Integer participantes, Refrigerio refrigerio, LocalDateTime fechaReserva, EstadoReserva estado, Integer precioTotal) { }
}
