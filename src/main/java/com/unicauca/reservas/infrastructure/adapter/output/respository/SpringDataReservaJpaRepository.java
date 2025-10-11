package com.unicauca.reservas.infrastructure.adapter.output.respository;

import com.unicauca.reservas.infrastructure.adapter.output.entity.ReservaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataReservaJpaRepository extends JpaRepository<ReservaJpaEntity, UUID> {
}
