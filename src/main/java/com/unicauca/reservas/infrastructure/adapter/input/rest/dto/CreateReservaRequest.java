package com.unicauca.reservas.infrastructure.adapter.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CreateReservaRequest(
        String  plan,
        String  usuario,
        Integer  guia,
        Integer participantes,
        String refrigerio,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime fechaReserva,
        Integer precioTotal) {
}
