package com.unicauca.reservas.infrastructure.adapter.input.rest.mapper;

import com.unicauca.reservas.application.port.input.CreateReservaUseCase;
import com.unicauca.reservas.application.port.input.UpdateReservaUseCase;
import com.unicauca.reservas.domain.models.EstadoReserva;
import com.unicauca.reservas.domain.models.Reserva;
import com.unicauca.reservas.domain.models.ReservaId;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.CreateReservaRequest;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.ReservaResponse;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.UpdateReservaRequest;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    public CreateReservaUseCase.ReservaRequest toDomain(CreateReservaRequest request){
        return new CreateReservaUseCase.ReservaRequest(
                request.plan(),
                request.usuario(),
                request.guia(),
                request.participantes(),
                request.refrigerio(),
                request.fechaReserva(),
                request.precioTotal()
                );
    }

    public UpdateReservaUseCase.ReservaRequest toDomain(UpdateReservaRequest request, String id){
        return new UpdateReservaUseCase.ReservaRequest(
                ReservaId.fromString(id),
                request.plan(),
                request.usuario(),
                request.guia(),
                request.participantes(),
                request.refrigerio(),
                request.fechaReserva(),
                EstadoReserva.PENDIENTE, // Estado por defecto al actualizar, mejorar esto en el futuro
                request.precioTotal()
        );
    }

    public ReservaResponse toResponse(Reserva reserva) {
        return new ReservaResponse(
                reserva.getId().value(),
                reserva.getPlan(),
                reserva.getUsuario(),
                reserva.getGuia(),
                reserva.getParticipantes(),
                reserva.getRefrigerio(),
                reserva.getFechaReserva(),
                reserva.getPrecioTotal()
        );
    }
}
