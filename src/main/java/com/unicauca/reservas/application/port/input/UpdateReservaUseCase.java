package com.unicauca.reservas.application.port.input;

import com.unicauca.reservas.domain.models.*;

import java.time.LocalDateTime;

public interface UpdateReservaUseCase {
    boolean updateReserva(ReservaRequest reserva);

    record ReservaRequest(ReservaId id, Refrigerio refrigerio, LocalDateTime fechaReserva) { }
}
