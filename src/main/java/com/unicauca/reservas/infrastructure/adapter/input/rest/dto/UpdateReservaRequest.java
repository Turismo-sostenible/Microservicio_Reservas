package com.unicauca.reservas.infrastructure.adapter.input.rest.dto;

import com.unicauca.reservas.domain.models.Guia;
import com.unicauca.reservas.domain.models.Plan;
import com.unicauca.reservas.domain.models.Refrigerio;
import com.unicauca.reservas.domain.models.Usuario;

import java.time.LocalDateTime;

public record UpdateReservaRequest(Usuario usuario, Guia guia, Plan plan, Integer participantes, Refrigerio refrigerio, LocalDateTime fechaReserva, Integer precioTotal) {
}
