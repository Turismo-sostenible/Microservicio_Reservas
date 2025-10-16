package com.unicauca.reservas.infrastructure.adapter.output.mapper;


import com.unicauca.reservas.domain.event.GuiaCreadoEvent;
import com.unicauca.reservas.domain.models.EstadoGuia;
import com.unicauca.reservas.domain.models.Guia;
import com.unicauca.reservas.domain.models.Horario;
import com.unicauca.reservas.infrastructure.adapter.output.entity.GuiaJpaEntity;
import com.unicauca.reservas.infrastructure.adapter.output.entity.HorarioEmbeddable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GuiaJpaMapper {

    public GuiaJpaEntity toJpaEntity(Guia guia) {
        GuiaJpaEntity entity = new GuiaJpaEntity();
        entity.setId(guia.getId());
        entity.setNombre(guia.getNombre());
        entity.setEmail(guia.getEmail());
        entity.setTelefono(guia.getTelefono());
        entity.setEstado(GuiaJpaEntity.EstadoGuiaJpa.valueOf(guia.getEstado().name()));
        entity.setHorarios(guia.getHorarios().stream()
                .map(h -> new HorarioEmbeddable(h.fechaHoraInicio(), h.fechaHoraFin()))
                .collect(Collectors.toList()));
        return entity;
    }

    public Guia toDomainEntity(GuiaJpaEntity entity) {
        return Guia.reconstitute(
                entity.getId(), // Usamos hashCode para simular un UUID
                entity.getNombre(),
                entity.getEmail(),
                entity.getTelefono(),
                EstadoGuia.valueOf(entity.getEstado().name()),
                entity.getHorarios().stream()
                        .map(h -> new Horario(h.getFechaHoraInicio(), h.getFechaHoraFin()))
                        .collect(Collectors.toList())
        );
    }
}
