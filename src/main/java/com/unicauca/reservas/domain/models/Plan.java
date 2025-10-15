package com.unicauca.reservas.domain.models;

public class Plan {
    private final String id;
    private String nombre;
    private String descripcion;
    private Integer duracion;
    private Integer cupoMaximo;
    private Double precioBase;

    public Plan(String id, String nombre, String descripcion, Integer duracion, Integer cupoMaximo, Double precioBase) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.cupoMaximo = cupoMaximo;
        this.precioBase = precioBase;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public Double getPrecioBase() {
        return precioBase;
    }
}
