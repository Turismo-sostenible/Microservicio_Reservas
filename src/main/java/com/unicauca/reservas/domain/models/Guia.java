package com.unicauca.reservas.domain.models;

public class Guia {
    private final Integer id;
    private String nombre;
    private String email;

    public Guia(Integer id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

}
