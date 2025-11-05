package com.unicauca.reservas.infrastructure.adapter.output.respository;

import com.unicauca.reservas.application.port.output.GuiaRepositoryPort;
import com.unicauca.reservas.domain.models.Guia;
import com.unicauca.reservas.infrastructure.adapter.output.mapper.GuiaJpaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostgresGuiaRepositoryAdapter implements GuiaRepositoryPort {

    private final SpringDataGuiaJpaRepository jpaRepository;
    private final GuiaJpaMapper mapper;

    public PostgresGuiaRepositoryAdapter(SpringDataGuiaJpaRepository jpaRepository, GuiaJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Guia save(Guia guia) {
        var jpaEntity = mapper.toJpaEntity(guia);
        var entity = jpaRepository.save(jpaEntity);
        return mapper.toDomainEntity(entity);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<Guia> findById(Integer id) {
        return jpaRepository.findById(id).map(mapper::toDomainEntity);
    }

    @Override
    public List<Guia> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomainEntity).toList();
    }

    @Override
    public boolean update(Guia guia) {
        if (jpaRepository.existsById(Integer.parseInt(guia.getId()))) {
            var jpaEntity = mapper.toJpaEntity(guia);
            jpaRepository.save(jpaEntity);
            return true;
        }
        return false;
    }
}
