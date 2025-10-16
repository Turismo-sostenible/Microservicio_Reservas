package com.unicauca.reservas.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombres del Exchange y Routing Key del PUBLICADOR (guia-service)
    public static final String GUIAS_EVENTS_EXCHANGE = "guias.events.exchange";
    public static final String GUIAS_EVENTS_ROUTING_KEY = "guias.event.#";
    public static final String GUIAS_CREATED_NOTIFICATIONS_QUEUE = "guias.created.notifications.queue";

    public static final String PLAN_EVENTS_EXCHANGE = "plan.events.exchange";
    public static final String PLAN_EVENTS_ROUTING_KEY = "plan.event.#";
    public static final String PLAN_CREATED_NOTIFICATIONS_QUEUE = "plan.created.notifications.queue";

    // 1. Declaramos que conocemos el Exchange público (el tablón de anuncios)
    @Bean
    public TopicExchange guiasEventsExchange() {
        return new TopicExchange(GUIAS_EVENTS_EXCHANGE);
    }

    @Bean
    public Queue notificationsQueue() {
        return new Queue(GUIAS_CREATED_NOTIFICATIONS_QUEUE);
    }

    // Conectamos nuestro buzón al tablón de anuncios, pidiendo solo los mensajes que nos interesan.
    @Bean
    public Binding guiasBinding(@Qualifier("notificationsQueue") Queue queue, @Qualifier("guiasEventsExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(GUIAS_EVENTS_ROUTING_KEY);
    }

    // Le decimos a Spring cómo convertir el JSON de los mensajes a nuestros objetos Java.
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // --- Configuración para PLAN ---
    @Bean
    public TopicExchange planEventsExchange() {
        return new TopicExchange(PLAN_EVENTS_EXCHANGE);
    }

    @Bean
    public Queue planNotificationsQueue() {
        return new Queue(PLAN_CREATED_NOTIFICATIONS_QUEUE);
    }

    // Conectamos nuestro buzón al tablón de anuncios, pidiendo solo los mensajes que nos interesan.
    @Bean
    public Binding planBinding(@Qualifier("planNotificationsQueue") Queue planQueue, @Qualifier("planEventsExchange") TopicExchange planExchange) {
        return BindingBuilder.bind(planQueue).to(planExchange).with(PLAN_EVENTS_ROUTING_KEY);
    }

    // Le decimos a Spring cómo convertir el JSON de los mensajes a nuestros objetos Java.
    @Bean
    public MessageConverter planJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
