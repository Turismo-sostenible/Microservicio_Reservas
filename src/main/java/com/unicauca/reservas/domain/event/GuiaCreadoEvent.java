package com.unicauca.reservas.domain.event;

import java.util.UUID;

// Este es el nuevo "contrato" enriquecido del evento
public record GuiaCreadoEvent(
        String guiaId,
        String nombre,
        String email,
        String telefono,
        String estado // Usamos String para que sea fácilmente serializable
) {
    // Mantenemos el record anidado para la estructura del ID
    //public record GuiaId(Integer value) {}
}