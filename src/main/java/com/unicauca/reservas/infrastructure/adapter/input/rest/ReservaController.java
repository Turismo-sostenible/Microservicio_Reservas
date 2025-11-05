package com.unicauca.reservas.infrastructure.adapter.input.rest;

import com.unicauca.reservas.application.port.input.CreateReservaUseCase;
import com.unicauca.reservas.application.port.input.DeleteReservaUseCase;
import com.unicauca.reservas.application.port.input.FindReservaUseCase;
import com.unicauca.reservas.application.port.input.UpdateReservaUseCase;
import com.unicauca.reservas.domain.models.ReservaId;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.CreateReservaRequest;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.ReservaResponse;
import com.unicauca.reservas.infrastructure.adapter.input.rest.dto.UpdateReservaRequest;
import com.unicauca.reservas.infrastructure.adapter.input.rest.mapper.ReservaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping()
@Tag(name = "Reservas", description = "API para la gestión de reservas de planes turísticos")
public class ReservaController {

    private final CreateReservaUseCase createReservaUseCase;
    private final FindReservaUseCase findReservaUseCase;
    private final UpdateReservaUseCase updateReservaUseCase;
    private final DeleteReservaUseCase deleteReservaUseCase;
    private final ReservaMapper reservaMapper;

    public ReservaController(CreateReservaUseCase createReservaUseCase, FindReservaUseCase findReservaUseCase,
                             UpdateReservaUseCase updateReservaUseCase, DeleteReservaUseCase deleteReservaUseCase,
                             ReservaMapper reservaMapper) {
        this.createReservaUseCase = createReservaUseCase;
        this.findReservaUseCase = findReservaUseCase;
        this.updateReservaUseCase = updateReservaUseCase;
        this.deleteReservaUseCase = deleteReservaUseCase;
        this.reservaMapper = reservaMapper;
    }

    @GetMapping
    @Operation(
            summary = "Listar todas las reservas",
            description = "Obtiene una lista de todas las reservas registradas en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de reservas obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = ReservaResponse.class))
    )
    public ResponseEntity<List<ReservaResponse>> getAllReservas() {
        List<ReservaResponse> response = findReservaUseCase.findAll().stream()
                .map(reservaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener reserva por ID",
            description = "Obtiene los detalles de una reserva específica mediante su identificador único"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva encontrada",
                    content = @Content(schema = @Schema(implementation = ReservaResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<ReservaResponse> getReservaById(
            @Parameter(description = "ID único de la reserva", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        return findReservaUseCase.findById(ReservaId.fromString(id))
                .map(reservaMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Crear nueva reserva",
            description = "Crea una nueva reserva de plan turístico en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Reserva creada exitosamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content
            )
    })
    public ResponseEntity<Void> createReserva(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la reserva a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateReservaRequest.class))
            )
            @RequestBody CreateReservaRequest request) {
        var reserva = reservaMapper.toDomain(request);
        var nuevaReserva = createReservaUseCase.createReserva(reserva);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaReserva.getId().value().toString())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar reserva existente",
            description = "Actualiza los datos de una reserva existente en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva actualizada exitosamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content
            )
    })
    public ResponseEntity<Void> updateReserva(
            @Parameter(description = "ID único de la reserva", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la reserva",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateReservaRequest.class))
            )
            @RequestBody UpdateReservaRequest request) {
        var reserva = reservaMapper.toDomain(request, id);
        boolean updatedReserva = updateReservaUseCase.updateReserva(reserva);
        return updatedReserva ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar reserva",
            description = "Elimina una reserva del sistema mediante su identificador único"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva eliminada exitosamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteReserva(
            @Parameter(description = "ID único de la reserva", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        deleteReservaUseCase.deleteReserva(ReservaId.fromString(id));
        return ResponseEntity.ok().build();
    }
}