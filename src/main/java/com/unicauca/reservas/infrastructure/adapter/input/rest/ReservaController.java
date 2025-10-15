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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/reservas")
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
    public ResponseEntity<List<ReservaResponse>> getAllReservas() {
        List<ReservaResponse> response = findReservaUseCase.findAll().stream().map(reservaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> getReservaById(@PathVariable String id) {
        return findReservaUseCase.findById(ReservaId.fromString(id)).map(reservaMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createReserva(@RequestBody CreateReservaRequest request) {
        var reserva = reservaMapper.toDomain(request);
        var nuevaReserva = createReservaUseCase.createReserva(reserva);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaReserva.getId().value().toString())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReserva(@PathVariable String id, @RequestBody UpdateReservaRequest request) {
        var reserva = reservaMapper.toDomain(request, id);
        boolean updatedReserva = updateReservaUseCase.updateReserva(reserva);
        return updatedReserva ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable String id) {
        deleteReservaUseCase.deleteReserva(ReservaId.fromString(id));
        return ResponseEntity.ok().build();
    }
}
