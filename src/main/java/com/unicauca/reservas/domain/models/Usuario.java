package com.unicauca.reservas.domain.models;

import java.util.Random;

public class Usuario {
    private final Integer id;
    private String nombre;
    private String email;

    public Usuario(Integer id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }
}
