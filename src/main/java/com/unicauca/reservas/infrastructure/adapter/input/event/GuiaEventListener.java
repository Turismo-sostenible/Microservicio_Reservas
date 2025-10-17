package com.unicauca.reservas.infrastructure.adapter.input.event;

import com.unicauca.reservas.domain.event.GuiaCreadoEvent;
import com.unicauca.reservas.domain.models.EstadoGuia;
import com.unicauca.reservas.domain.models.Guia;
import com.unicauca.reservas.infrastructure.adapter.output.respository.PostgresGuiaRepositoryAdapter;
import com.unicauca.reservas.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GuiaEventListener {

    private static final Logger log = LoggerFactory.getLogger(GuiaEventListener.class);
    private final PostgresGuiaRepositoryAdapter guiaRepository;

    public GuiaEventListener(PostgresGuiaRepositoryAdapter guiaRepository) {
        this.guiaRepository = guiaRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.GUIAS_CREATED_NOTIFICATIONS_QUEUE)
    public void handleGuiaCreado(GuiaCreadoEvent event) {
        log.info("EVENTO ENRIQUECIDO 'Guia Creado' RECIBIDO. ID: {}", event.guiaId());

        // --- AHORA TIENES ACCESO A TODO EL OBJETO ---
        System.out.println("======================================================");
        System.out.println("Enviando email de bienvenida al nuevo guía:");
        System.out.println("Nombre: " + event.nombre());
        System.out.println("Email: " + event.email()); // <-- Puedes usar el email
        System.out.println("Teléfono: " + event.telefono()); // <-- y el teléfono
        System.out.println("Estado: " + event.estado()); // <-- y el estado
        System.out.println("======================================================");

        Guia guia = new Guia(
                event.guiaId(),
                event.nombre(),
                event.email(),
                event.telefono(),
                EstadoGuia.valueOf(event.estado())
        );

        // Guardar la entidad Guia en la base de datos
        Guia savedGuia = guiaRepository.save(guia);
        log.info("Guía guardada con éxito: {}", savedGuia.getId());
    }
}
