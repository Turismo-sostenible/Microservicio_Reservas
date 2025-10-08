package com.unicauca.reservas.infrastructure.config;

import com.unicauca.reservas.application.port.input.CreateReservaUseCase;
import com.unicauca.reservas.application.port.input.DeleteReservaUseCase;
import com.unicauca.reservas.application.port.input.FindReservaUseCase;
import com.unicauca.reservas.application.port.input.UpdateReservaUseCase;
import com.unicauca.reservas.application.port.output.ReservaRepositoryPort;
import com.unicauca.reservas.application.usecases.CreateReservaUseCaseImpl;
import com.unicauca.reservas.application.usecases.DeleteReservaUseCaseImpl;
import com.unicauca.reservas.application.usecases.FindReservaUseCaseImpl;
import com.unicauca.reservas.application.usecases.UpdateReservaUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CreateReservaUseCase createReservaUseCase(ReservaRepositoryPort reservaRepositoryPort) {
        return new CreateReservaUseCaseImpl(reservaRepositoryPort);
    }

    @Bean
    public UpdateReservaUseCase updateReservaUseCase(ReservaRepositoryPort reservaRepositoryPort) {
        return new UpdateReservaUseCaseImpl(reservaRepositoryPort);
    }

    @Bean
    public DeleteReservaUseCase deleteReservaUseCase(ReservaRepositoryPort reservaRepositoryPort) {
        return new DeleteReservaUseCaseImpl(reservaRepositoryPort);
    }

    @Bean
    public FindReservaUseCase findReservaUseCase(ReservaRepositoryPort reservaRepositoryPort) {
        return new FindReservaUseCaseImpl(reservaRepositoryPort);
    }
}
