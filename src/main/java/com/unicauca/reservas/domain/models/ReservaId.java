package com.unicauca.reservas.domain.models;

import java.io.Serializable;
import java.util.UUID;

public record ReservaId(UUID value) implements Serializable {

    public static ReservaId generate() {
        return new ReservaId(UUID.randomUUID());
    }

    public static ReservaId fromString(String value) {
        return new ReservaId(UUID.fromString(value));
    }
}
