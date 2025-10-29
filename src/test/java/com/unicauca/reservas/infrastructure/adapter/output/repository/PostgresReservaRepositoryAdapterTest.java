package com.unicauca.reservas.infrastructure.adapter.output.repository;

import com.unicauca.reservas.domain.models.*;
import com.unicauca.reservas.infrastructure.adapter.output.entity.ReservaJpaEntity;
import com.unicauca.reservas.infrastructure.adapter.output.mapper.ReservaJpaMapper;
import com.unicauca.reservas.infrastructure.adapter.output.respository.PostgresReservaRepositoryAdapter;
import com.unicauca.reservas.infrastructure.adapter.output.respository.SpringDataReservaJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostgresReservaRepositoryAdapterTest {

    @Mock
    private SpringDataReservaJpaRepository jpaRepository;

    @Mock
    private ReservaJpaMapper mapper;

    @InjectMocks
    private PostgresReservaRepositoryAdapter repository;

    private Reserva reserva;
    private ReservaJpaEntity jpaEntity;
    private UUID reservaId;

    @BeforeEach
    void setUp() {
        reservaId = UUID.randomUUID();

        // Crear reserva de dominio
        reserva = Reserva.reconstruct(
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

        // Crear entidad JPA
        jpaEntity = new ReservaJpaEntity();
        jpaEntity.setId(reservaId);
        jpaEntity.setUsuarioId("usuario123");
        jpaEntity.setGuiaId(1);
        jpaEntity.setPlanId("plan123");
        jpaEntity.setParticipantes(3);
        jpaEntity.setRefrigerio(ReservaJpaEntity.RefrigerioJpa.CENA);
        jpaEntity.setFechaReserva(LocalDateTime.parse("2025-10-19T21:13:39"));
        jpaEntity.setEstado(ReservaJpaEntity.EstadoJpa.PENDIENTE);
        jpaEntity.setPrecioTotal(300);
    }

    @Test
    @DisplayName("save - Debería guardar una reserva nueva")
    void deberiaSaveReserva() {
        when(mapper.toJpaEntity(reserva)).thenReturn(jpaEntity);
        when(jpaRepository.save(jpaEntity)).thenReturn(jpaEntity);
        when(mapper.toDomainEntity(jpaEntity)).thenReturn(reserva);

        Reserva resultado = repository.save(reserva);

        assertNotNull(resultado);
        assertEquals(reservaId, resultado.getId().value());
        verify(mapper, times(1)).toJpaEntity(reserva);
        verify(jpaRepository, times(1)).save(jpaEntity);
        verify(mapper, times(1)).toDomainEntity(jpaEntity);
    }

    @Test
    @DisplayName("deleteById - Debería eliminar reserva por ID")
    void deberiaDeleteById() {
        ReservaId id = new ReservaId(reservaId);

        doNothing().when(jpaRepository).deleteById(reservaId);

        repository.deleteById(id);

        verify(jpaRepository, times(1)).deleteById(reservaId);
    }

    @Test
    @DisplayName("findById - Debería encontrar reserva por ID cuando existe")
    void deberiaFindByIdCuandoExiste() {
        ReservaId id = new ReservaId(reservaId);

        when(jpaRepository.findById(reservaId)).thenReturn(Optional.of(jpaEntity));
        when(mapper.toDomainEntity(jpaEntity)).thenReturn(reserva);

        Optional<Reserva> resultado = repository.findById(id);

        assertTrue(resultado.isPresent());
        assertEquals(reservaId, resultado.get().getId().value());
        verify(jpaRepository, times(1)).findById(reservaId);
        verify(mapper, times(1)).toDomainEntity(jpaEntity);
    }

    @Test
    @DisplayName("findById - Debería retornar Optional.empty cuando no existe")
    void deberiaRetornarEmptyCuandoNoExiste() {
        ReservaId id = new ReservaId(reservaId);

        when(jpaRepository.findById(reservaId)).thenReturn(Optional.empty());

        Optional<Reserva> resultado = repository.findById(id);

        assertFalse(resultado.isPresent());
        verify(jpaRepository, times(1)).findById(reservaId);
        verify(mapper, never()).toDomainEntity(any());
    }

    @Test
    @DisplayName("findAll - Debería retornar todas las reservas")
    void deberiaFindAll() {
        ReservaJpaEntity jpaEntity2 = new ReservaJpaEntity();
        jpaEntity2.setId(UUID.randomUUID());
        jpaEntity2.setUsuarioId("usuario456");
        jpaEntity2.setGuiaId(2);
        jpaEntity2.setPlanId("plan456");
        jpaEntity2.setParticipantes(2);
        jpaEntity2.setRefrigerio(ReservaJpaEntity.RefrigerioJpa.ALMUERZO);
        jpaEntity2.setFechaReserva(LocalDateTime.parse("2025-11-20T15:30:00"));
        jpaEntity2.setEstado(ReservaJpaEntity.EstadoJpa.PENDIENTE);
        jpaEntity2.setPrecioTotal(200);

        Reserva reserva2 = Reserva.reconstruct(
                new ReservaId(jpaEntity2.getId()),
                new Usuario("usuario456", "Pedro", "Lopez", "pedro@example.com"),
                new Guia(2, "Carlos", "carlos@example.com", "555-5678", EstadoGuia.ACTIVO),
                new Plan("plan456", "Tour Nocturno", "Tour por la noche", 3, 15, 80.0),
                2,
                Refrigerio.ALMUERZO,
                LocalDateTime.parse("2025-11-20T15:30:00"),
                EstadoReserva.PENDIENTE,
                200
        );

        List<ReservaJpaEntity> jpaEntities = List.of(jpaEntity, jpaEntity2);

        when(jpaRepository.findAll()).thenReturn(jpaEntities);
        when(mapper.toDomainEntity(jpaEntity)).thenReturn(reserva);
        when(mapper.toDomainEntity(jpaEntity2)).thenReturn(reserva2);

        List<Reserva> resultado = repository.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(jpaRepository, times(1)).findAll();
        verify(mapper, times(2)).toDomainEntity(any(ReservaJpaEntity.class));
    }

    @Test
    @DisplayName("findAll - Debería retornar lista vacía cuando no hay reservas")
    void deberiaRetornarListaVaciaCuandoNoHayReservas() {
        when(jpaRepository.findAll()).thenReturn(List.of());

        List<Reserva> resultado = repository.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(jpaRepository, times(1)).findAll();
        verify(mapper, never()).toDomainEntity(any());
    }

    @Test
    @DisplayName("update - Debería actualizar reserva cuando existe")
    void deberiaUpdateCuandoExiste() {
        when(jpaRepository.existsById(reservaId)).thenReturn(true);
        when(mapper.toJpaEntityUpdate(reserva)).thenReturn(jpaEntity);
        when(jpaRepository.save(jpaEntity)).thenReturn(jpaEntity);

        boolean resultado = repository.update(reserva);

        assertTrue(resultado);
        verify(jpaRepository, times(1)).existsById(reservaId);
        verify(mapper, times(1)).toJpaEntityUpdate(reserva);
        verify(jpaRepository, times(1)).save(jpaEntity);
    }

    @Test
    @DisplayName("update - Debería retornar false cuando no existe")
    void deberiaRetornarFalseCuandoNoExisteParaUpdate() {
        when(jpaRepository.existsById(reservaId)).thenReturn(false);

        boolean resultado = repository.update(reserva);

        assertFalse(resultado);
        verify(jpaRepository, times(1)).existsById(reservaId);
        verify(mapper, never()).toJpaEntityUpdate(any());
        verify(jpaRepository, never()).save(any());
    }

    @Test
    @DisplayName("update - Debería usar toJpaEntityUpdate en lugar de toJpaEntity")
    void deberiaUsarToJpaEntityUpdateEnUpdate() {
        when(jpaRepository.existsById(reservaId)).thenReturn(true);
        when(mapper.toJpaEntityUpdate(reserva)).thenReturn(jpaEntity);
        when(jpaRepository.save(jpaEntity)).thenReturn(jpaEntity);

        repository.update(reserva);

        verify(mapper, times(1)).toJpaEntityUpdate(reserva);
        verify(mapper, never()).toJpaEntity(any());
    }
}
