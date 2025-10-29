package com.unicauca.reservas.domain.model;

import com.unicauca.reservas.domain.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    private Usuario usuario;
    private Guia guia;
    private Plan plan;
    private Integer participantes;
    private Refrigerio refrigerio;
    private LocalDateTime fechaReserva;
    private Integer precioTotal;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("usuario123", "Juan", "Perez", "juan@example.com");
        guia = new Guia(1, "Maria", "maria@example.com", "555-1234", EstadoGuia.ACTIVO);
        plan = new Plan("plan123", "Tour Ciudad", "Descripción del tour", 5, 20, 100.0);
        participantes = 3;
        refrigerio = Refrigerio.CENA;
        fechaReserva = LocalDateTime.parse("2025-10-19T21:13:39");
        precioTotal = 300;
    }

    @Test
    @DisplayName("Constructor - Debería crear una reserva con estado PENDIENTE por defecto")
    void deberiaCrearReservaConEstadoPendiente() {
        ReservaId id = ReservaId.generate();

        Reserva reserva = new Reserva(id, usuario, guia, plan, participantes, refrigerio, fechaReserva, precioTotal);

        assertNotNull(reserva);
        assertEquals(id, reserva.getId());
        assertEquals(usuario, reserva.getUsuario());
        assertEquals(guia, reserva.getGuia());
        assertEquals(plan, reserva.getPlan());
        assertEquals(participantes, reserva.getParticipantes());
        assertEquals(refrigerio, reserva.getRefrigerio());
        assertEquals(fechaReserva, reserva.getFechaReserva());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        assertEquals(precioTotal, reserva.getPrecioTotal());
    }

    @Test
    @DisplayName("create - Debería crear una reserva nueva con ID generado y estado PENDIENTE")
    void deberiaCrearReservaNueva() {
        Reserva reserva = Reserva.create(usuario, guia, plan, participantes, refrigerio, fechaReserva, precioTotal);

        assertNotNull(reserva);
        assertNotNull(reserva.getId());
        assertNotNull(reserva.getId().value());
        assertEquals(usuario, reserva.getUsuario());
        assertEquals(guia, reserva.getGuia());
        assertEquals(plan, reserva.getPlan());
        assertEquals(participantes, reserva.getParticipantes());
        assertEquals(refrigerio, reserva.getRefrigerio());
        assertEquals(fechaReserva, reserva.getFechaReserva());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        assertEquals(precioTotal, reserva.getPrecioTotal());
    }

    @Test
    @DisplayName("create - Debería generar IDs únicos para diferentes reservas")
    void deberiaGenerarIdsUnicosParaDiferentesReservas() {
        Reserva reserva1 = Reserva.create(usuario, guia, plan, participantes, refrigerio, fechaReserva, precioTotal);
        Reserva reserva2 = Reserva.create(usuario, guia, plan, participantes, refrigerio, fechaReserva, precioTotal);

        assertNotEquals(reserva1.getId().value(), reserva2.getId().value());
    }

    @Test
    @DisplayName("reconstruct - Debería reconstruir una reserva existente con estado específico")
    void deberiaReconstruirReservaExistente() {
        ReservaId id = ReservaId.generate();
        EstadoReserva estadoEsperado = EstadoReserva.PENDIENTE;

        Reserva reserva = Reserva.reconstruct(
                id, usuario, guia, plan, participantes, refrigerio, fechaReserva, estadoEsperado, precioTotal
        );

        assertNotNull(reserva);
        assertEquals(id, reserva.getId());
        assertEquals(usuario, reserva.getUsuario());
        assertEquals(guia, reserva.getGuia());
        assertEquals(plan, reserva.getPlan());
        assertEquals(participantes, reserva.getParticipantes());
        assertEquals(refrigerio, reserva.getRefrigerio());
        assertEquals(fechaReserva, reserva.getFechaReserva());
        assertEquals(estadoEsperado, reserva.getEstado());
        assertEquals(precioTotal, reserva.getPrecioTotal());
    }

    @Test
    @DisplayName("reconstruct - Debería permitir reconstruir con cualquier estado")
    void deberiaReconstruirConCualquierEstado() {
        ReservaId id = ReservaId.generate();

        for (EstadoReserva estado : EstadoReserva.values()) {
            Reserva reserva = Reserva.reconstruct(
                    id, usuario, guia, plan, participantes, refrigerio, fechaReserva, estado, precioTotal
            );

            assertEquals(estado, reserva.getEstado(),
                    "Debería reconstruir con estado " + estado.name());
        }
    }

    @Test
    @DisplayName("actualizarReserva - Debería actualizar todos los campos excepto ID y estado")
    void deberiaActualizarReserva() {
        ReservaId idOriginal = ReservaId.generate();
        Reserva reservaOriginal = Reserva.reconstruct(
                idOriginal, usuario, guia, plan, participantes, refrigerio, fechaReserva, EstadoReserva.PENDIENTE, precioTotal
        );

        // Crear datos actualizados
        Usuario nuevoUsuario = new Usuario("usuario456", "Pedro", "Lopez", "pedro@example.com");
        Guia nuevoGuia = new Guia(2, "Carlos", "carlos@example.com", "555-5678", EstadoGuia.ACTIVO);
        Plan nuevoPlan = new Plan("plan456", "Tour Nocturno", "Tour por la noche", 3, 15, 80.0);
        Integer nuevosParticipantes = 5;
        Refrigerio nuevoRefrigerio = Refrigerio.ALMUERZO;
        LocalDateTime nuevaFecha = LocalDateTime.parse("2025-11-20T15:30:00");
        Integer nuevoPrecio = 500;

        Reserva reservaActualizada = new Reserva(
                ReservaId.generate(), nuevoUsuario, nuevoGuia, nuevoPlan,
                nuevosParticipantes, nuevoRefrigerio, nuevaFecha, nuevoPrecio
        );

        // Actualizar
        reservaOriginal.actualizarReserva(reservaActualizada);

        // Verificar que se actualizaron los campos
        assertEquals(idOriginal, reservaOriginal.getId()); // ID no cambia
        assertEquals(EstadoReserva.PENDIENTE, reservaOriginal.getEstado()); // Estado no cambia
        assertEquals(nuevoUsuario, reservaOriginal.getUsuario());
        assertEquals(nuevoGuia, reservaOriginal.getGuia());
        assertEquals(nuevoPlan, reservaOriginal.getPlan());
        assertEquals(nuevosParticipantes, reservaOriginal.getParticipantes());
        assertEquals(nuevoRefrigerio, reservaOriginal.getRefrigerio());
        assertEquals(nuevaFecha, reservaOriginal.getFechaReserva());
        assertEquals(nuevoPrecio, reservaOriginal.getPrecioTotal());
    }

    @Test
    @DisplayName("actualizarReserva - No debería cambiar el ID de la reserva original")
    void noDeberiaActualizarId() {
        ReservaId idOriginal = ReservaId.generate();
        Reserva reservaOriginal = Reserva.create(usuario, guia, plan, participantes, refrigerio, fechaReserva, precioTotal);
        UUID idOriginalValue = reservaOriginal.getId().value();

        Reserva reservaActualizada = Reserva.create(
                new Usuario("usuario456", "Pedro", "Lopez", "pedro@example.com"),
                guia, plan, participantes, refrigerio, fechaReserva, precioTotal
        );

        reservaOriginal.actualizarReserva(reservaActualizada);

        assertEquals(idOriginalValue, reservaOriginal.getId().value());
    }

    @Test
    @DisplayName("actualizarReserva - No debería cambiar el estado de la reserva original")
    void noDeberiaActualizarEstado() {
        Reserva reservaOriginal = Reserva.reconstruct(
                ReservaId.generate(), usuario, guia, plan, participantes,
                refrigerio, fechaReserva, EstadoReserva.PENDIENTE, precioTotal
        );

        Reserva reservaActualizada = Reserva.reconstruct(
                ReservaId.generate(), usuario, guia, plan, participantes,
                refrigerio, fechaReserva, EstadoReserva.PENDIENTE, precioTotal
        );

        reservaOriginal.actualizarReserva(reservaActualizada);

        assertEquals(EstadoReserva.PENDIENTE, reservaOriginal.getEstado());
    }

    @Test
    @DisplayName("calcularPrecioTotal - Debería retornar null (método no implementado)")
    void calcularPrecioTotalDeberiaRetornarNull() {
        Reserva reserva = Reserva.create(usuario, guia, plan, participantes, refrigerio, fechaReserva, precioTotal);

        Integer resultado = reserva.calcularPrecioTotal();

        assertNull(resultado);
    }

    @Test
    @DisplayName("Getters - Deberían retornar los valores correctos")
    void gettersDeberianRetornarValoresCorrectos() {
        ReservaId id = ReservaId.generate();
        Reserva reserva = Reserva.reconstruct(
                id, usuario, guia, plan, participantes, refrigerio, fechaReserva, EstadoReserva.PENDIENTE, precioTotal
        );

        assertEquals(id, reserva.getId());
        assertEquals(usuario, reserva.getUsuario());
        assertEquals(guia, reserva.getGuia());
        assertEquals(plan, reserva.getPlan());
        assertEquals(participantes, reserva.getParticipantes());
        assertEquals(refrigerio, reserva.getRefrigerio());
        assertEquals(fechaReserva, reserva.getFechaReserva());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        assertEquals(precioTotal, reserva.getPrecioTotal());
    }

    @Test
    @DisplayName("create - Debería aceptar diferentes tipos de refrigerio")
    void deberiaCrearConDiferentesRefrigerios() {
        for (Refrigerio ref : Refrigerio.values()) {
            Reserva reserva = Reserva.create(usuario, guia, plan, participantes, ref, fechaReserva, precioTotal);

            assertEquals(ref, reserva.getRefrigerio());
        }
    }
}
