package com.unicauca.reservas.infrastructure.adapter.output.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservas")
@Data
public class ReservaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @Column(name = "guia_id", nullable = false)
    private Integer guiaId;

    @Column(name = "plan_id", nullable = false)
    private String planId;

    @Column(nullable = false)
    private Integer participantes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RefrigerioJpa refrigerio;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoJpa estado;

    @Column(name = "precio_total", nullable = false)
    private Integer precioTotal;

    public enum EstadoJpa {
        PENDIENTE,
        CONFIRMADA,
        CANCELADA
    }

    public enum RefrigerioJpa {
        DESAYUNO,
        ALMUERZO,
        CENA,
        MERIENDA
    }

}
