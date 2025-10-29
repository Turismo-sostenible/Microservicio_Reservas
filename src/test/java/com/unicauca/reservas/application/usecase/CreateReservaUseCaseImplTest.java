package com.unicauca.reservas.application.usecase;

import com.unicauca.reservas.application.port.input.CreateReservaUseCase;
import com.unicauca.reservas.application.port.output.ReservaRepositoryPort;
import com.unicauca.reservas.application.usecases.CreateReservaUseCaseImpl;
import com.unicauca.reservas.domain.models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CreateReservaUseCaseImplTest {

    @Mock
    private ReservaRepositoryPort reservaRepository;

    @InjectMocks
    private CreateReservaUseCaseImpl createReservaUseCase;

    @Test
    @DisplayName("Debería crear Reserva, guardarlo y publicar evento")
    void deberiaCrearGuardarYPublicar() {
        CreateReservaUseCase.ReservaRequest command = new CreateReservaUseCase.ReservaRequest(
                "plan123",
                "usuario123",
                1,
                5,
                "CENA",
                LocalDateTime.parse("2025-10-19T21:13:39"),
                15000
        );

        //Crear una reserva esperada para el mock
        Reserva reservaEsperada = Reserva.create(
                new Usuario("usuario123", null, null, null),
                new Guia(1, null, null, null, null),
                new Plan("plan123", null, null, null, null, null),
                5,
                Refrigerio.CENA,
                LocalDateTime.parse("2025-10-19T21:13:39"),
                15000
        );

        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaEsperada);

        Reserva resultado = createReservaUseCase.createReserva(command);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("usuario123", resultado.getUsuario().getId());
        assertEquals(1, resultado.getGuia().getId());
        assertEquals("plan123", resultado.getPlan().getId());
        assertEquals(5, resultado.getParticipantes());
        assertEquals(Refrigerio.CENA, resultado.getRefrigerio());
        assertEquals(LocalDateTime.parse("2025-10-19T21:13:39"), resultado.getFechaReserva());
        assertEquals(15000, resultado.getPrecioTotal());

        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }
}
