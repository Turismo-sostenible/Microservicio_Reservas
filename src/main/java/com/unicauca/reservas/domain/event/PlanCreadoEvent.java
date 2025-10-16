package com.unicauca.reservas.domain.event;

public record PlanCreadoEvent(
        String id,
        String nombre,
        String descripcion,
        Integer duracion,
        Integer cupoMaximo,
        Double precioBase
) {
    public record PlanId(String value) {}
}
