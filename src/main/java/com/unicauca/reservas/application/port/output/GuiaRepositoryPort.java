package com.unicauca.reservas.application.port.output;

import com.unicauca.reservas.domain.models.Guia;

import java.util.List;
import java.util.Optional;

public interface GuiaRepositoryPort {
    Guia save(Guia guia);
    void deleteById(Integer id);
    Optional<Guia> findById(Integer id);
    List<Guia> findAll();
    boolean update(Guia guia);
}
