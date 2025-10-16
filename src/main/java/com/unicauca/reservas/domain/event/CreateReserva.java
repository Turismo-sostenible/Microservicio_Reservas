package com.unicauca.reservas.domain.event;

import com.unicauca.reservas.domain.models.*;

import java.time.LocalDateTime;

public record CreateReserva(
        ReservaId reservaId,
        Usuario usuario,
        Guia guia,
        Plan plan,
        Integer participantes,
        Refrigerio refrigerio,
        LocalDateTime fechaReserva,
        Integer precioTotal) {
}
