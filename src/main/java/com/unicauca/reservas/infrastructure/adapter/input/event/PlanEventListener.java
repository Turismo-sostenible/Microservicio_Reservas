package com.unicauca.reservas.infrastructure.adapter.input.event;

import com.unicauca.reservas.domain.event.GuiaCreadoEvent;
import com.unicauca.reservas.domain.event.PlanCreadoEvent;
import com.unicauca.reservas.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PlanEventListener {
    private static final Logger log = LoggerFactory.getLogger(PlanEventListener.class);

    @RabbitListener(queues = RabbitMQConfig.PLAN_CREATED_NOTIFICATIONS_QUEUE)
    public void handleGuiaCreado(PlanCreadoEvent event) {
        log.info("EVENTO ENRIQUECIDO 'plan Creado' RECIBIDO. ID: {}", event.id());

        // --- AHORA TIENES ACCESO A TODO EL OBJETO ---
        System.out.println("======================================================");
        System.out.println("Enviando email de bienvenida al nuevo plan:");
        System.out.println("Nombre: " + event.nombre());
        System.out.println("Descripcion: " + event.descripcion()); // <-- Puedes usar el email
        System.out.println("Cupo: " + event.cupoMaximo()); // <-- y el teléfono
        System.out.println("Duracion: " + event.duracion()); // <-- y el teléfono
        System.out.println("Precio: " + event.precioBase()); // <-- y el estado
        System.out.println("======================================================");

        // Aquí podrías, por ejemplo, guardar este objeto completo en una base de datos
        // local del servicio de notificaciones si necesitaras un historial.

        log.info("Procesando notificación para el plan {}.", event.nombre());
    }
}
