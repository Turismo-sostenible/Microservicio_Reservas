package com.unicauca.reservas.application.port.input;

import com.unicauca.reservas.domain.models.ReservaId;

public interface DeleteReservaUseCase {
    void deleteReserva(ReservaId id);
}
