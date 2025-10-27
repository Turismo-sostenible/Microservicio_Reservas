package com.unicauca.reservas.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicauca.reservas.application.port.input.CreateReservaUseCase;
import com.unicauca.reservas.application.port.input.DeleteReservaUseCase;
import com.unicauca.reservas.application.port.input.FindReservaUseCase;
import com.unicauca.reservas.application.port.input.UpdateReservaUseCase;
import com.unicauca.reservas.domain.models.*;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.CreateReservaRequest;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.UpdateReservaRequest;
import com.unicauca.reservas.infrastructure.adapter.input.rest.mapper.ReservaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaController.class)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateReservaUseCase createReservaUseCase;

    @MockitoBean
    private FindReservaUseCase findReservaUseCase;

    @MockitoBean
    private UpdateReservaUseCase updateReservaUseCase;

    @MockitoBean
    private DeleteReservaUseCase deleteReservaUseCase;

    @MockitoBean
    private ReservaMapper reservaMapper;

    private Reserva reserva;
    private ReservaId reservaId;

    @BeforeEach
    void setUp() {
        reservaId = ReservaId.generate();
        reserva = Reserva.create(
                new Usuario("usuario123", "Juan", "Perez", "juan@example.com"),
                new Guia(1, "Maria", "maria@example.com", "555-1234", EstadoGuia.ACTIVO),
                new Plan("plan123", "Tour Ciudad", "Descripción del tour", 5, 20, 100.0),
                3,
                Refrigerio.CENA,
                LocalDateTime.parse("2025-10-19T21:13:39"),
                300
        );
    }

    @Test
    @DisplayName("GET / - Debería retornar todas las reservas")
    void deberiaRetornarTodasLasReservas() throws Exception {
        List<Reserva> reservas = List.of(reserva);
        when(findReservaUseCase.findAll()).thenReturn(reservas);
        when(reservaMapper.toResponse(any(Reserva.class))).thenReturn(
                new com.unicauca.reservas.infrastructure.adapter.input.rest.dto.ReservaResponse(
                        reserva.getId().value(),
                        reserva.getPlan(),
                        reserva.getUsuario(),
                        reserva.getGuia(),
                        reserva.getParticipantes(),
                        reserva.getRefrigerio(),
                        reserva.getFechaReserva(),
                        reserva.getPrecioTotal()
                )
        );

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(findReservaUseCase, times(1)).findAll();
        verify(reservaMapper, times(1)).toResponse(any(Reserva.class));
    }

    @Test
    @DisplayName("GET /{id} - Debería retornar reserva por ID")
    void deberiaRetornarReservaPorId() throws Exception {
        String id = reserva.getId().value().toString();
        when(findReservaUseCase.findById(any(ReservaId.class))).thenReturn(Optional.of(reserva));
        when(reservaMapper.toResponse(any(Reserva.class))).thenReturn(
                new com.unicauca.reservas.infrastructure.adapter.input.rest.dto.ReservaResponse(
                        reserva.getId().value(),
                        reserva.getPlan(),
                        reserva.getUsuario(),
                        reserva.getGuia(),
                        reserva.getParticipantes(),
                        reserva.getRefrigerio(),
                        reserva.getFechaReserva(),
                        reserva.getPrecioTotal()
                )
        );

        mockMvc.perform(get("/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reservaId").value(reserva.getId().value().toString()));

        verify(findReservaUseCase, times(1)).findById(any(ReservaId.class));
        verify(reservaMapper, times(1)).toResponse(any(Reserva.class));
    }

    @Test
    @DisplayName("GET /{id} - Debería retornar 404 cuando no existe")
    void deberiaRetornar404CuandoNoExiste() throws Exception {
        String id = UUID.randomUUID().toString();
        when(findReservaUseCase.findById(any(ReservaId.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/{id}", id))
                .andExpect(status().isNotFound());

        verify(findReservaUseCase, times(1)).findById(any(ReservaId.class));
        verify(reservaMapper, never()).toResponse(any(Reserva.class));
    }

    @Test
    @DisplayName("POST / - Debería crear nueva reserva")
    void deberiaCrearNuevaReserva() throws Exception {
        CreateReservaRequest request = new CreateReservaRequest(
                "plan123",
                "usuario123",
                1,
                3,
                "CENA",
                LocalDateTime.parse("2025-10-19T21:13:39"),
                300
        );

        CreateReservaUseCase.ReservaRequest command = new CreateReservaUseCase.ReservaRequest(
                request.plan(),
                request.usuario(),
                request.guia(),
                request.participantes(),
                request.refrigerio(),
                request.fechaReserva(),
                request.precioTotal()
        );

        when(reservaMapper.toDomain(any(CreateReservaRequest.class))).thenReturn(command);
        when(createReservaUseCase.createReserva(any(CreateReservaUseCase.ReservaRequest.class))).thenReturn(reserva);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location",
                        "http://localhost/" + reserva.getId().value().toString()));

        verify(reservaMapper, times(1)).toDomain(any(CreateReservaRequest.class));
        verify(createReservaUseCase, times(1)).createReserva(any(CreateReservaUseCase.ReservaRequest.class));
    }

    @Test
    @DisplayName("PUT /{id} - Debería actualizar reserva existente")
    void deberiaActualizarReservaExistente() throws Exception {
        String id = reserva.getId().value().toString();
        UpdateReservaRequest request = new UpdateReservaRequest(
                Refrigerio.ALMUERZO,
                LocalDateTime.parse("2025-11-20T15:30:00")
        );

        UpdateReservaUseCase.ReservaRequest command = new UpdateReservaUseCase.ReservaRequest(
                reserva.getId(),
                request.refrigerio(),
                request.fechaReserva()
        );

        when(reservaMapper.toDomain(any(UpdateReservaRequest.class), eq(id))).thenReturn(command);
        when(updateReservaUseCase.updateReserva(any(UpdateReservaUseCase.ReservaRequest.class))).thenReturn(true);

        mockMvc.perform(put("/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(reservaMapper, times(1)).toDomain(any(UpdateReservaRequest.class), eq(id));
        verify(updateReservaUseCase, times(1)).updateReserva(any(UpdateReservaUseCase.ReservaRequest.class));
    }

    @Test
    @DisplayName("PUT /{id} - Debería retornar 404 cuando no existe")
    void deberiaRetornar404AlActualizarNoExistente() throws Exception {
        String id = UUID.randomUUID().toString();
        UpdateReservaRequest request = new UpdateReservaRequest(
                Refrigerio.ALMUERZO,
                LocalDateTime.parse("2025-11-20T15:30:00")
        );

        UpdateReservaUseCase.ReservaRequest command = new UpdateReservaUseCase.ReservaRequest(
                ReservaId.fromString(id),
                request.refrigerio(),
                request.fechaReserva()
        );

        when(reservaMapper.toDomain(any(UpdateReservaRequest.class), eq(id))).thenReturn(command);
        when(updateReservaUseCase.updateReserva(any(UpdateReservaUseCase.ReservaRequest.class))).thenReturn(false);

        mockMvc.perform(put("/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(reservaMapper, times(1)).toDomain(any(UpdateReservaRequest.class), eq(id));
        verify(updateReservaUseCase, times(1)).updateReserva(any(UpdateReservaUseCase.ReservaRequest.class));
    }

    @Test
    @DisplayName("DELETE /{id} - Debería eliminar reserva")
    void deberiaEliminarReserva() throws Exception {
        String id = reserva.getId().value().toString();

        doNothing().when(deleteReservaUseCase).deleteReserva(any(ReservaId.class));

        mockMvc.perform(delete("/{id}", id))
                .andExpect(status().isOk());

        verify(deleteReservaUseCase, times(1)).deleteReserva(any(ReservaId.class));
    }
}
