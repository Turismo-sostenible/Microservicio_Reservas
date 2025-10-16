package com.unicauca.reservas.infrastructure.adapter.output.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "guias")
@Data
public class GuiaJpaEntity {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoGuiaJpa estado;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "guia_horarios", joinColumns = @JoinColumn(name = "guia_id"))
    private List<HorarioEmbeddable> horarios;

    public enum EstadoGuiaJpa {
        ACTIVO,
        INACTIVO
    }
}
