package com.unicauca.reservas.domain.event;

import com.unicauca.reservas.domain.models.EstadoReserva;
import com.unicauca.reservas.domain.models.ReservaId;

public record UpdateReserva (ReservaId reservaId, EstadoReserva estado){
}
