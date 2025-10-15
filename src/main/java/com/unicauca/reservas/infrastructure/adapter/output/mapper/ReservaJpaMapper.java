package com.unicauca.reservas.infrastructure.adapter.output.mapper;

import com.unicauca.reservas.domain.models.*;
import com.unicauca.reservas.infrastructure.adapter.output.entity.ReservaJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservaJpaMapper {

    public ReservaJpaEntity toJpaEntity(Reserva reserva) {
        ReservaJpaEntity entity = new ReservaJpaEntity();
        //entity.setId(reserva.getId().value());
        entity.setUsuarioId(reserva.getUsuario().getId());
        entity.setGuiaId(Integer.parseInt(reserva.getGuia().getId().toString()));
        entity.setPlanId(reserva.getPlan().getId());
        entity.setParticipantes(reserva.getParticipantes());
        entity.setRefrigerio(ReservaJpaEntity.RefrigerioJpa.valueOf(reserva.getRefrigerio().name()));
        entity.setFechaReserva(reserva.getFechaReserva());
        entity.setEstado(ReservaJpaEntity.EstadoJpa.valueOf(reserva.getEstado().name()));
        entity.setPrecioTotal(reserva.getPrecioTotal());
        return entity;
    }

    public ReservaJpaEntity toJpaEntityUpdate(Reserva reserva) {
        ReservaJpaEntity entity = new ReservaJpaEntity();
        entity.setId(reserva.getId().value());
        entity.setUsuarioId(reserva.getUsuario().getId());
        entity.setGuiaId(Integer.parseInt(reserva.getGuia().getId().toString()));
        entity.setPlanId(reserva.getPlan().getId());
        entity.setParticipantes(reserva.getParticipantes());
        entity.setRefrigerio(ReservaJpaEntity.RefrigerioJpa.valueOf(reserva.getRefrigerio().name()));
        entity.setFechaReserva(reserva.getFechaReserva());
        entity.setEstado(ReservaJpaEntity.EstadoJpa.valueOf(reserva.getEstado().name()));
        entity.setPrecioTotal(reserva.getPrecioTotal());
        return entity;
    }

    public Reserva toDomainEntity(ReservaJpaEntity entity) {
        return Reserva.reconstruct(
                new ReservaId(entity.getId()),
                new Usuario(entity.getUsuarioId(), null, null, null),
                new Guia(entity.getGuiaId(), null),
                new Plan(entity.getPlanId(), null, null, null, null, null),
                entity.getParticipantes(),
                Refrigerio.valueOf(entity.getRefrigerio().name()),
                entity.getFechaReserva().toLocalDate().atStartOfDay(),
                EstadoReserva.valueOf(entity.getEstado().name()),
                entity.getPrecioTotal()
        );
    }
}
