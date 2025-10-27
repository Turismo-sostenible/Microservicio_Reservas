package com.unicauca.reservas.infrastructure.adapter.output.mapper;

import com.unicauca.reservas.domain.models.*;
import com.unicauca.reservas.infrastructure.adapter.output.entity.ReservaJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservaJpaMapperTest {

    private ReservaJpaMapper mapper;
    private Reserva reserva;
    private ReservaJpaEntity entity;
    private UUID reservaId;

    @BeforeEach
    void setUp() {
        mapper = new ReservaJpaMapper();
        reservaId = UUID.randomUUID();

        // Crear una reserva de dominio
        reserva = Reserva.create(
                new Usuario("usuario123", "Juan", "Perez", "juan@example.com"),
                new Guia(1, "Maria", "maria@example.com", "555-1234", EstadoGuia.ACTIVO),
                new Plan("plan123", "Tour Ciudad", "Descripción del tour", 5, 20, 100.0),
                3,
                Refrigerio.CENA,
                LocalDateTime.parse("2025-10-19T21:13:39"),
                300
        );

        // Crear una entidad JPA
        entity = new ReservaJpaEntity();
        entity.setId(reservaId);
        entity.setUsuarioId("usuario123");
        entity.setGuiaId(1);
        entity.setPlanId("plan123");
        entity.setParticipantes(3);
        entity.setRefrigerio(ReservaJpaEntity.RefrigerioJpa.CENA);
        entity.setFechaReserva(LocalDateTime.parse("2025-10-19T00:00:00"));
        entity.setEstado(ReservaJpaEntity.EstadoJpa.PENDIENTE);
        entity.setPrecioTotal(300);
    }

    @Test
    @DisplayName("Debería mapear Reserva de dominio a ReservaJpaEntity")
    void deberiaMapearDominioAJpaEntity() {
        ReservaJpaEntity result = mapper.toJpaEntity(reserva);

        assertNotNull(result);
        assertNull(result.getId()); // El ID no se setea en toJpaEntity
        assertEquals("usuario123", result.getUsuarioId());
        assertEquals(1, result.getGuiaId());
        assertEquals("plan123", result.getPlanId());
        assertEquals(3, result.getParticipantes());
        assertEquals(ReservaJpaEntity.RefrigerioJpa.CENA, result.getRefrigerio());
        assertEquals(LocalDateTime.parse("2025-10-19T21:13:39"), result.getFechaReserva());
        assertEquals(ReservaJpaEntity.EstadoJpa.PENDIENTE, result.getEstado());
        assertEquals(300, result.getPrecioTotal());
    }

    @Test
    @DisplayName("Debería mapear Reserva de dominio a ReservaJpaEntity con ID para update")
    void deberiaMapearDominioAJpaEntityParaUpdate() {
        // Usar reconstruct para crear una reserva con ID específico
        Reserva reservaConId = Reserva.reconstruct(
                new ReservaId(reservaId),
                new Usuario("usuario123", "Juan", "Perez", "juan@example.com"),
                new Guia(1, "Maria", "maria@example.com", "555-1234", EstadoGuia.ACTIVO),
                new Plan("plan123", "Tour Ciudad", "Descripción del tour", 5, 20, 100.0),
                3,
                Refrigerio.CENA,
                LocalDateTime.parse("2025-10-19T21:13:39"),
                EstadoReserva.PENDIENTE,
                300
        );

        ReservaJpaEntity result = mapper.toJpaEntityUpdate(reservaConId);

        assertNotNull(result);
        assertEquals(reservaId, result.getId()); // El ID SÍ se setea en toJpaEntityUpdate
        assertEquals("usuario123", result.getUsuarioId());
        assertEquals(1, result.getGuiaId());
        assertEquals("plan123", result.getPlanId());
        assertEquals(3, result.getParticipantes());
        assertEquals(ReservaJpaEntity.RefrigerioJpa.CENA, result.getRefrigerio());
        assertEquals(LocalDateTime.parse("2025-10-19T21:13:39"), result.getFechaReserva());
        assertEquals(ReservaJpaEntity.EstadoJpa.PENDIENTE, result.getEstado());
        assertEquals(300, result.getPrecioTotal());
    }

    @Test
    @DisplayName("Debería mapear ReservaJpaEntity a Reserva de dominio")
    void deberiaMapearJpaEntityADominio() {
        Reserva result = mapper.toDomainEntity(entity);

        assertNotNull(result);
        assertEquals(reservaId, result.getId().value());
        assertEquals("usuario123", result.getUsuario().getId());
        assertEquals(1, result.getGuia().getId());
        assertEquals("plan123", result.getPlan().getId());
        assertEquals(3, result.getParticipantes());
        assertEquals(Refrigerio.CENA, result.getRefrigerio());
        assertEquals(LocalDateTime.parse("2025-10-19T00:00:00"), result.getFechaReserva());
        assertEquals(EstadoReserva.PENDIENTE, result.getEstado());
        assertEquals(300, result.getPrecioTotal());
    }

    @Test
    @DisplayName("Debería mapear todos los valores de Refrigerio correctamente")
    void deberiaMapearTodosLosRefrigerios() {
        for (Refrigerio refrigerio : Refrigerio.values()) {
            Reserva reservaTemp = Reserva.create(
                    new Usuario("usuario123", "Juan", "Perez", "juan@example.com"),
                    new Guia(1, "Maria", "maria@example.com", "555-1234", EstadoGuia.ACTIVO),
                    new Plan("plan123", "Tour Ciudad", "Descripción del tour", 5, 20, 100.0),
                    3,
                    refrigerio,
                    LocalDateTime.parse("2025-10-19T21:13:39"),
                    300
            );

            ReservaJpaEntity result = mapper.toJpaEntity(reservaTemp);

            assertEquals(
                    ReservaJpaEntity.RefrigerioJpa.valueOf(refrigerio.name()),
                    result.getRefrigerio(),
                    "Debería mapear " + refrigerio.name() + " correctamente"
            );
        }
    }

    @Test
    @DisplayName("Debería mapear todos los valores de EstadoReserva correctamente")
    void deberiaMapearTodosLosEstados() {
        for (EstadoReserva estado : EstadoReserva.values()) {
            Reserva reservaTemp = Reserva.reconstruct(
                    new ReservaId(reservaId),
                    new Usuario("usuario123", "Juan", "Perez", "juan@example.com"),
                    new Guia(1, "Maria", "maria@example.com", "555-1234", EstadoGuia.ACTIVO),
                    new Plan("plan123", "Tour Ciudad", "Descripción del tour", 5, 20, 100.0),
                    3,
                    Refrigerio.CENA,
                    LocalDateTime.parse("2025-10-19T21:13:39"),
                    estado,
                    300
            );

            ReservaJpaEntity result = mapper.toJpaEntityUpdate(reservaTemp);

            assertEquals(
                    ReservaJpaEntity.EstadoJpa.valueOf(estado.name()),
                    result.getEstado(),
                    "Debería mapear " + estado.name() + " correctamente"
            );
        }
    }

    @Test
    @DisplayName("Debería convertir fechaReserva a inicio del día en toDomainEntity")
    void deberiaConvertirFechaAInicioDeDia() {
        entity.setFechaReserva(LocalDateTime.parse("2025-10-19T15:30:45"));

        Reserva result = mapper.toDomainEntity(entity);

        assertEquals(
                LocalDateTime.parse("2025-10-19T00:00:00"),
                result.getFechaReserva(),
                "La fecha debería convertirse al inicio del día"
        );
    }

    @Test
    @DisplayName("Debería manejar conversión de GuiaId de Integer a String correctamente")
    void deberiaManejarConversionGuiaId() {
        Reserva reservaConGuia = Reserva.create(
                new Usuario("usuario123", "Juan", "Perez", "juan@example.com"),
                new Guia(999, "Maria", "maria@example.com", "555-1234", EstadoGuia.ACTIVO),
                new Plan("plan123", "Tour Ciudad", "Descripción del tour", 5, 20, 100.0),
                3,
                Refrigerio.CENA,
                LocalDateTime.parse("2025-10-19T21:13:39"),
                300
        );

        ReservaJpaEntity result = mapper.toJpaEntity(reservaConGuia);

        assertEquals(999, result.getGuiaId());
    }
}
