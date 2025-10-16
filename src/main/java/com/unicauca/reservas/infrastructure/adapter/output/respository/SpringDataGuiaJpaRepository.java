package com.unicauca.reservas.infrastructure.adapter.output.respository;

import com.unicauca.reservas.infrastructure.adapter.output.entity.GuiaJpaEntity;
import com.unicauca.reservas.infrastructure.adapter.output.entity.ReservaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataGuiaJpaRepository extends JpaRepository<GuiaJpaEntity, Integer> {
}
