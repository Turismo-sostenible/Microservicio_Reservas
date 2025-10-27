package com.unicauca.reservas.infrastructure.adapter.input.mapper;

import com.unicauca.reservas.application.port.input.CreateReservaUseCase;
import com.unicauca.reservas.application.port.input.UpdateReservaUseCase;
import com.unicauca.reservas.domain.models.*;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.CreateReservaRequest;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.ReservaResponse;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.UpdateReservaRequest;
import com.unicauca.reservas.infrastructure.adapter.input.rest.mapper.ReservaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservaMapperTest {
    private ReservaMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ReservaMapper();
    }

    @Test
    @DisplayName("Debería mapear CreateReservaRequest a CreateReserva")
    void deberiaMapearCreateRequestACommand() {
        CreateReservaRequest request = new CreateReservaRequest(
                "plan1",
                "usuario1",
                1,
                3,
                "CENA",
                LocalDateTime.parse("2025-10-19T21:13:39"),
                1234);

        CreateReservaUseCase.ReservaRequest command = mapper.toDomain(request);

        assertEquals("plan1", command.plan());
        assertEquals("usuario1", command.usuario());
        assertEquals(1, command.guia());
        assertEquals(3, command.participantes());
        assertEquals("CENA", command.refrigerio());
        assertEquals(LocalDateTime.parse("2025-10-19T21:13:39"), command.fechaReserva());
        assertEquals(1234, command.precioTotal());
    }

    @Test
    @DisplayName("Debería mapear UpdateReservaRequest a UpdateReserva")
    void deberiaMapearUpdateRequestACommand() {
        UpdateReservaRequest request = new UpdateReservaRequest(
                Refrigerio.DESAYUNO,
                LocalDateTime.parse("2025-10-26T21:13:39"));
        String id = "f47ac10b-58cc-4372-a567-0e02b2c3d479"; // mirar esto

        UpdateReservaUseCase.ReservaRequest command = mapper.toDomain(request, id);

        assertEquals(id, command.id().value().toString());
        assertEquals(Refrigerio.DESAYUNO, command.refrigerio());
        assertEquals(LocalDateTime.parse("2025-10-26T21:13:39"), command.fechaReserva());
    }

    @Test
    @DisplayName("Debería mapear Reserva (Dominio) a ReservaResponse")
    void deberiaMapearDominioAResponse() {
        Reserva reserva = Reserva.create(
                new Usuario("1", "juan", "perez", "juanp@example.com"),
                new Guia(1, "alberto", "alberto@example.com","123456789",EstadoGuia.ACTIVO),
                new Plan("1", "plan aves", "sendero de avistamiento de aves", 3, 20, 123.45),
                3,
                Refrigerio.CENA,
                LocalDateTime.parse("2025-10-19T21:13:39"),
                1234);

        ReservaResponse response = mapper.toResponse(reserva);

        assertEquals(reserva.getId().value(), response.reservaId());

        // Assertions para Plan
        assertEquals("1", response.plan().getId());
        assertEquals("plan aves", response.plan().getNombre());
        assertEquals("sendero de avistamiento de aves", response.plan().getDescripcion());
        assertEquals(3, response.plan().getDuracion());
        assertEquals(20, response.plan().getCupoMaximo());
        assertEquals(123.45, response.plan().getPrecioBase());

        // Assertions para Usuario
        assertEquals("1", response.usuario().getId());
        assertEquals("juan", response.usuario().getNombre());
        assertEquals("perez", response.usuario().getApellido());
        assertEquals("juanp@example.com", response.usuario().getEmail());

        // Assertions para Guia
        assertEquals(1, response.guia().getId());
        assertEquals("alberto", response.guia().getNombre());
        assertEquals("alberto@example.com", response.guia().getEmail());
        assertEquals("123456789", response.guia().getTelefono());
        assertEquals(EstadoGuia.ACTIVO, response.guia().getEstado());

        // Assertions para campos simples
        assertEquals(3, response.participantes());
        assertEquals(Refrigerio.CENA, response.refrigerio());
        assertEquals(LocalDateTime.parse("2025-10-19T21:13:39"), response.fechaReserva());
        assertEquals(1234, response.precioTotal());
    }
}
