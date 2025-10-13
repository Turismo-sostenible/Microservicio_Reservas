package com.unicauca.reservas.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Guia {
    private final Integer id;
    private String nombre;
    private EstadoGuia estado;
    private final List<Horario> horarios;

    public Guia(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.estado = EstadoGuia.ACTIVO;
        this.horarios = new ArrayList<>();
    }

    public static Guia reconstitute(Integer id, String nombre, EstadoGuia estado, List<Horario> horarios) {
        Guia guia = new Guia(id, nombre);
        guia.estado = estado;
        guia.horarios.addAll(horarios);
        return guia;
    }

    /**
     * Factory method para la creación de un nuevo Guía, asegurando un estado
     * inicial válido.
     */
    public static Guia crear(String nombre) {
        // Aquí se pueden añadir más validaciones si es necesario
        return new Guia(ThreadLocalRandom.current().nextInt(0, 10000), nombre);
    }

    // --- Lógica de Negocio ---

    public void agregarHorario(Horario nuevoHorario) {
        if (this.estado == EstadoGuia.INACTIVO) {
            throw new IllegalStateException("No se puede agregar un horario a un guía inactivo.");
        }

        // Regla de negocio: No permitir horarios que se solapen.
        boolean seSolapa = horarios.stream().anyMatch(h -> h.seSolapaCon(nuevoHorario));
        if (seSolapa) {
            throw new IllegalArgumentException("El nuevo horario se solapa con uno existente.");
        }
        this.horarios.add(nuevoHorario);
    }

    public void actualizarDatos(String nuevoNombre) {
        this.nombre = Objects.requireNonNull(nuevoNombre, "El nombre no puede ser nulo.");
    }

    public void desactivar() {
        if (this.estado == EstadoGuia.INACTIVO) {
            throw new IllegalStateException("El guía ya se encuentra inactivo.");
        }
        this.estado = EstadoGuia.INACTIVO;
    }

    public void activar() {
        if (this.estado == EstadoGuia.ACTIVO) {
            throw new IllegalStateException("El guía ya se encuentra activo.");
        }
        this.estado = EstadoGuia.ACTIVO;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public EstadoGuia getEstado() {
        return estado;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getId() {
        return id;
    }
}
