package com.unicauca.reservas.application.usecase;

import com.unicauca.reservas.application.port.input.UpdateReservaUseCase;
import com.unicauca.reservas.application.port.output.ReservaRepositoryPort;
import com.unicauca.reservas.application.usecases.UpdateReservaUseCaseImpl;
import com.unicauca.reservas.domain.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateReservaUseCaseImplTest {

    @Mock
    private ReservaRepositoryPort reservaRepository;

    @InjectMocks
    private UpdateReservaUseCaseImpl updateReservaUseCase;

    private Reserva reservaExistente;

    @BeforeEach
    void setUp() {
        reservaExistente = Reserva.create(
                new Usuario("usuario123", "Juan Perez", "", ""),
                new Guia(123, "Maria Gomez", "Experta en historia local", "555-1234", EstadoGuia.ACTIVO),
                new Plan("plan123", "Tour por la ciudad", "Descripcion del plan", 5000, 120, 123.124),
                1,
                Refrigerio.CENA,
                LocalDateTime.parse("2025-10-19T21:13:39"),
                15000
        );
    }

    @Test
    @DisplayName("Debería actualizar Reserva")
    void deberiaActualizarYPublicarSiExiste() {
        ReservaId id = reservaExistente.getId();
        UpdateReservaUseCase.ReservaRequest command = new UpdateReservaUseCase.ReservaRequest(
                id,  Refrigerio.ALMUERZO, LocalDateTime.parse("2025-11-20T15:30:00")
        );

        when(reservaRepository.findById(id)).thenReturn(Optional.of(reservaExistente));
        when(reservaRepository.update(any(Reserva.class))).thenReturn(true);

        boolean resultado = updateReservaUseCase.updateReserva(command);

        assertTrue(resultado);
        verify(reservaRepository, times(1)).findById(id);
        verify(reservaRepository, times(1)).update(reservaExistente);

        //Verificar que los campos se actualizaron
        assertEquals(Refrigerio.ALMUERZO, reservaExistente.getRefrigerio());
        assertEquals(LocalDateTime.parse("2025-11-20T15:30:00"), reservaExistente.getFechaReserva());
    }

    @Test
    @DisplayName("Debería retornar false si Reserva no existe")
    void deberiaRetornarFalseSiNoExiste() {
        ReservaId id = ReservaId.generate();
        UpdateReservaUseCase.ReservaRequest command = new UpdateReservaUseCase.ReservaRequest(
                id, Refrigerio.ALMUERZO, LocalDateTime.parse("2025-11-20T15:30:00")
        );

        when(reservaRepository.findById(id)).thenReturn(Optional.empty());

        boolean resultado = updateReservaUseCase.updateReserva(command);

        assertFalse(resultado);
        verify(reservaRepository, times(1)).findById(id);
        verify(reservaRepository, never()).save(any());
    }
}