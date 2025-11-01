package com.unicauca.reservas;

import com.unicauca.reservas.domain.models.Refrigerio;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.CreateReservaRequest;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.ReservaResponse;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.UpdateReservaRequest;
import com.unicauca.reservas.infrastructure.adapter.output.entity.ReservaJpaEntity;
import com.unicauca.reservas.infrastructure.adapter.output.respository.SpringDataReservaJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.cloud.bootstrap.enabled=false"
)
class ReservaFullFlowE2ETest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SpringDataReservaJpaRepository reservaRepository;

    @AfterEach
    void limpiar() {
        reservaRepository.deleteAll();
    }

    @Test
    @DisplayName("E2E: Debería crear una Reserva, guardarla en Postgres y recuperarla por API")
    void deberiaCrearReservaYVerificarGuardadoCompleto() {
        // Arrange - Preparar datos de prueba
        CreateReservaRequest request = new CreateReservaRequest(
                "plan123",
                "usuario123",
                1,
                3,
                "CENA",
                LocalDateTime.parse("2025-10-19T21:13:39"),
                300
        );

        // Act - Crear reserva via API REST
        ResponseEntity<Void> createResponse = restTemplate.postForEntity(
                "/api/reservas",
                request,
                Void.class
        );

        // Assert - Verificar creación exitosa
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        URI location = createResponse.getHeaders().getLocation();
        assertNotNull(location, "El header Location debe estar presente");

        // Extraer ID de la URL
        String[] pathSegments = location.getPath().split("/");
        String reservaId = pathSegments[pathSegments.length - 1];
        assertNotNull(reservaId, "El ID de la reserva no debe ser nulo");

        // Verificar que se guardó en la base de datos
        Optional<ReservaJpaEntity> jpaEntity = reservaRepository.findById(UUID.fromString(reservaId));
        assertTrue(jpaEntity.isPresent(), "La reserva debe estar guardada en Postgres");
        assertEquals("usuario123", jpaEntity.get().getUsuarioId());
        assertEquals(1, jpaEntity.get().getGuiaId());
        assertEquals("plan123", jpaEntity.get().getPlanId());
        assertEquals(3, jpaEntity.get().getParticipantes());
        assertEquals(ReservaJpaEntity.RefrigerioJpa.CENA, jpaEntity.get().getRefrigerio());
        assertEquals(300, jpaEntity.get().getPrecioTotal());

        // Verificar que se puede recuperar via API
        ResponseEntity<ReservaResponse> getResponse = restTemplate.getForEntity(
                "/api/reservas/{id}",
                ReservaResponse.class,
                reservaId
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(reservaId, getResponse.getBody().reservaId().toString());
        assertEquals("usuario123", getResponse.getBody().usuario().getId());
        assertEquals(1, getResponse.getBody().guia().getId());
        assertEquals("plan123", getResponse.getBody().plan().getId());
        assertEquals(3, getResponse.getBody().participantes());
        assertEquals(Refrigerio.CENA, getResponse.getBody().refrigerio());
        assertEquals(300, getResponse.getBody().precioTotal());
    }

    @Test
    @DisplayName("E2E: Debería actualizar una Reserva existente")
    void deberiaActualizarReservaExistente() {
        // Arrange - Crear reserva inicial
        CreateReservaRequest createRequest = new CreateReservaRequest(
                "plan123",
                "usuario123",
                1,
                3,
                "CENA",
                LocalDateTime.parse("2025-10-19T21:13:39"),
                300
        );

        ResponseEntity<Void> createResponse = restTemplate.postForEntity(
                "/api/reservas",
                createRequest,
                Void.class
        );

        URI location = createResponse.getHeaders().getLocation();
        String reservaId = location.getPath().split("/")[location.getPath().split("/").length - 1];

        // Act - Actualizar la reserva
        UpdateReservaRequest updateRequest = new UpdateReservaRequest(
                Refrigerio.ALMUERZO,
                LocalDateTime.parse("2025-11-20T15:30:00")
        );

        ResponseEntity<Void> updateResponse = restTemplate.exchange(
                "/api/reservas/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                Void.class,
                reservaId
        );

        // Assert - Verificar actualización exitosa
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());

        // Verificar en base de datos
        await().atMost(3, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    Optional<ReservaJpaEntity> updatedEntity = reservaRepository.findById(UUID.fromString(reservaId));
                    assertTrue(updatedEntity.isPresent());
                    assertEquals(ReservaJpaEntity.RefrigerioJpa.ALMUERZO, updatedEntity.get().getRefrigerio());
                });

        // Verificar via API
        ResponseEntity<ReservaResponse> getResponse = restTemplate.getForEntity(
                "/api/reservas/{id}",
                ReservaResponse.class,
                reservaId
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(Refrigerio.ALMUERZO, getResponse.getBody().refrigerio());
    }

    @Test
    @DisplayName("E2E: Debería eliminar una Reserva existente")
    void deberiaEliminarReservaExistente() {
        // Arrange - Crear reserva
        CreateReservaRequest createRequest = new CreateReservaRequest(
                "plan123",
                "usuario123",
                1,
                3,
                "CENA",
                LocalDateTime.parse("2025-10-19T21:13:39"),
                300
        );

        ResponseEntity<Void> createResponse = restTemplate.postForEntity(
                "/api/reservas",
                createRequest,
                Void.class
        );

        URI location = createResponse.getHeaders().getLocation();
        String reservaId = location.getPath().split("/")[location.getPath().split("/").length - 1];

        // Verificar que existe
        assertTrue(reservaRepository.findById(UUID.fromString(reservaId)).isPresent());

        // Act - Eliminar la reserva
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/reservas/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                reservaId
        );

        // Assert - Verificar eliminación exitosa
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        // Verificar que ya no existe en la base de datos
        await().atMost(3, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    Optional<ReservaJpaEntity> deletedEntity = reservaRepository.findById(UUID.fromString(reservaId));
                    assertFalse(deletedEntity.isPresent(), "La reserva debe haber sido eliminada");
                });

        // Verificar que retorna 404 via API
        ResponseEntity<ReservaResponse> getResponse = restTemplate.getForEntity(
                "/api/reservas/{id}",
                ReservaResponse.class,
                reservaId
        );

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    @DisplayName("E2E: Debería listar todas las Reservas")
    void deberiaListarTodasLasReservas() {
        // Arrange - Crear múltiples reservas
        CreateReservaRequest request1 = new CreateReservaRequest(
                "plan123",
                "usuario123",
                1,
                3,
                "CENA",
                LocalDateTime.parse("2025-10-19T21:13:39"),
                300
        );

        CreateReservaRequest request2 = new CreateReservaRequest(
                "plan456",
                "usuario456",
                2,
                5,
                "ALMUERZO",
                LocalDateTime.parse("2025-11-20T15:30:00"),
                500
        );

        restTemplate.postForEntity("/api/reservas", request1, Void.class);
        restTemplate.postForEntity("/api/reservas", request2, Void.class);

        // Act - Listar todas las reservas
        ResponseEntity<ReservaResponse[]> getResponse = restTemplate.getForEntity(
                "/api/reservas",
                ReservaResponse[].class
        );

        // Assert - Verificar lista de reservas
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(2, getResponse.getBody().length);

        List<ReservaResponse> reservas = List.of(getResponse.getBody());
        assertTrue(reservas.stream().anyMatch(r -> r.usuario().getId().equals("usuario123")));
        assertTrue(reservas.stream().anyMatch(r -> r.usuario().getId().equals("usuario456")));
    }

    @Test
    @DisplayName("E2E: Debería retornar 404 al buscar Reserva inexistente")
    void deberiaRetornar404CuandoReservaNoExiste() {
        // Arrange
        String idInexistente = UUID.randomUUID().toString();

        // Act
        ResponseEntity<ReservaResponse> getResponse = restTemplate.getForEntity(
                "/api/reservas/{id}",
                ReservaResponse.class,
                idInexistente
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    @DisplayName("E2E: Debería retornar 404 al actualizar Reserva inexistente")
    void deberiaRetornar404AlActualizarReservaInexistente() {
        // Arrange
        String idInexistente = UUID.randomUUID().toString();
        UpdateReservaRequest updateRequest = new UpdateReservaRequest(
                Refrigerio.ALMUERZO,
                LocalDateTime.parse("2025-11-20T15:30:00")
        );

        // Act
        ResponseEntity<Void> updateResponse = restTemplate.exchange(
                "/api/reservas/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                Void.class,
                idInexistente
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, updateResponse.getStatusCode());
    }

    @Test
    @DisplayName("E2E: Flujo completo - Crear, Actualizar, Consultar y Eliminar")
    void flujoCompletoReserva() {
        // 1. Crear
        CreateReservaRequest createRequest = new CreateReservaRequest(
                "plan123",
                "usuario123",
                1,
                3,
                "CENA",
                LocalDateTime.parse("2025-10-19T21:13:39"),
                300
        );

        ResponseEntity<Void> createResponse = restTemplate.postForEntity(
                "/api/reservas",
                createRequest,
                Void.class
        );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        String reservaId = createResponse.getHeaders().getLocation().getPath()
                .split("/")[createResponse.getHeaders().getLocation().getPath().split("/").length - 1];

        // 2. Consultar
        ResponseEntity<ReservaResponse> getResponse1 = restTemplate.getForEntity(
                "/api/reservas/{id}",
                ReservaResponse.class,
                reservaId
        );

        assertEquals(HttpStatus.OK, getResponse1.getStatusCode());
        assertEquals(Refrigerio.CENA, getResponse1.getBody().refrigerio());

        // 3. Actualizar
        UpdateReservaRequest updateRequest = new UpdateReservaRequest(
                Refrigerio.DESAYUNO,
                LocalDateTime.parse("2025-12-25T08:00:00")
        );

        ResponseEntity<Void> updateResponse = restTemplate.exchange(
                "/api/reservas/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                Void.class,
                reservaId
        );

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());

        // 4. Consultar actualizada
        ResponseEntity<ReservaResponse> getResponse2 = restTemplate.getForEntity(
                "/api/reservas/{id}",
                ReservaResponse.class,
                reservaId
        );

        assertEquals(HttpStatus.OK, getResponse2.getStatusCode());
        assertEquals(Refrigerio.DESAYUNO, getResponse2.getBody().refrigerio());

        // 5. Eliminar
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/reservas/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                reservaId
        );

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        // 6. Verificar eliminación
        await().atMost(3, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    ResponseEntity<ReservaResponse> getResponse3 = restTemplate.getForEntity(
                            "/api/reservas/{id}",
                            ReservaResponse.class,
                            reservaId
                    );
                    assertEquals(HttpStatus.NOT_FOUND, getResponse3.getStatusCode());
                });
    }
}
