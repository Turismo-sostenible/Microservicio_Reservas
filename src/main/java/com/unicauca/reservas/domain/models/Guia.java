package com.unicauca.reservas.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Guia {
    private final Integer id;
    private String nombre;
    private String email;
    private String telefono;
    private EstadoGuia estado;
    private final List<Horario> horarios;

    public Guia(Integer id, String nombre, String email, String telefono, EstadoGuia estado) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.estado = estado;
        this.horarios = new ArrayList<>();
    }

    public static Guia reconstitute(Integer id, String nombre, String email, String telefono, EstadoGuia estado, List<Horario> horarios) {
        Guia guia = new Guia(id, nombre, email, telefono, estado);
        guia.horarios.addAll(horarios);
        return guia;
    }

    /**
     * Factory method para la creación de un nuevo Guía, asegurando un estado
     * inicial válido.
     */
    public static Guia crear(String nombre, String email, String telefono, EstadoGuia estado) {
        // Aquí se pueden añadir más validaciones si es necesario
        return new Guia(ThreadLocalRandom.current().nextInt(0, 10000), nombre, email, telefono, estado);
    }

    // --- Lógica de Negocio ---

    public void actualizarDatos(String nuevoNombre) {
        this.nombre = Objects.requireNonNull(nuevoNombre, "El nombre no puede ser nulo.");
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public EstadoGuia getEstado() {
        return estado;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }
}
