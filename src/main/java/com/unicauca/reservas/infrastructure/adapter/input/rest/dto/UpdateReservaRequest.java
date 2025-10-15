package com.unicauca.reservas.infrastructure.adapter.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unicauca.reservas.domain.models.Refrigerio;

import java.time.LocalDateTime;

public record UpdateReservaRequest( Refrigerio refrigerio, @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime fechaReserva) {
}
