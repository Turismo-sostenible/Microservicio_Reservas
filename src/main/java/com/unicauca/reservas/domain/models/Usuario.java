package com.unicauca.reservas.domain.models;

public class Usuario {
    private final String id;
    private String nombre;
    private String apellido;
    private String email;

    public Usuario(String id, String nombre, String apellido, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }
}
