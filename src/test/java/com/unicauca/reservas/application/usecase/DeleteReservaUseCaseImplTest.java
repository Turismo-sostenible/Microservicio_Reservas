package com.unicauca.reservas.application.usecase;

import com.unicauca.reservas.application.port.output.ReservaRepositoryPort;
import com.unicauca.reservas.application.usecases.DeleteReservaUseCaseImpl;
import com.unicauca.reservas.domain.models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteReservaUseCaseImplTest {

    @Mock
    private ReservaRepositoryPort reservaRepository;

    @InjectMocks
    private DeleteReservaUseCaseImpl deleteReservaUseCase;

    @Test
    @DisplayName("Debería llamar a deleteById")
    void deberiaEliminar() {
        ReservaId id = ReservaId.generate();

        doNothing().when(reservaRepository).deleteById(id);

        deleteReservaUseCase.deleteReserva(id);

        verify(reservaRepository, times(1)).deleteById(id);
    }
}
