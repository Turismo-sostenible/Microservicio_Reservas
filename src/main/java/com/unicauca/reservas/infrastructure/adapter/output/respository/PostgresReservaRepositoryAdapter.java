package com.unicauca.reservas.infrastructure.adapter.output.respository;

import com.unicauca.reservas.application.port.output.ReservaRepositoryPort;
import com.unicauca.reservas.domain.models.Reserva;
import com.unicauca.reservas.domain.models.ReservaId;
import com.unicauca.reservas.infrastructure.adapter.output.mapper.ReservaJpaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostgresReservaRepositoryAdapter implements ReservaRepositoryPort {

    private final SpringDataReservaJpaRepository jpaRepository;
    private final ReservaJpaMapper mapper;

    public PostgresReservaRepositoryAdapter(SpringDataReservaJpaRepository jpaRepository, ReservaJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }


    @Override
    public Reserva save(Reserva reserva) {
        var jpaEntity = mapper.toJpaEntity(reserva);
        var entity = jpaRepository.save(jpaEntity);
        return mapper.toDomainEntity(entity);
    }

    @Override
    public void deleteById(ReservaId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public Optional<Reserva> findById(ReservaId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomainEntity);
    }

    @Override
    public List<Reserva> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomainEntity).toList();
    }

    @Override
    public boolean update(Reserva reserva) {
        if (jpaRepository.existsById(reserva.getId().value())) {
            var jpaEntity = mapper.toJpaEntity(reserva);
            jpaRepository.save(jpaEntity);
            return true;
        }
        return false;
    }
}
