package com.unicauca.reservas.domain.models;

import java.time.LocalDateTime;

public class Reserva {

    private final ReservaId id;
    private Usuario usuario;
    private Guia guia;
    private Plan plan;
    private Integer participantes;
    private Refrigerio refrigerio;
    private LocalDateTime fechaReserva;
    private EstadoReserva estado;
    private Integer precioTotal;

    public Reserva(ReservaId id, Usuario usuario, Guia guia, Plan plan, Integer participantes, Refrigerio refrigerio, LocalDateTime fechaReserva, Integer precioTotal) {
        this.id = id;
        this.usuario = usuario;
        this.guia = guia;
        this.plan = plan;
        this.participantes = participantes;
        this.refrigerio = refrigerio;
        this.fechaReserva = fechaReserva;
        this.estado = EstadoReserva.PENDIENTE;
        this.precioTotal = precioTotal;
    }

    //logica de negocio

    public Integer calcularPrecioTotal() {
        //Integer precioPlan = plan.getPrecio();
        //Integer precioRefrigerio = refrigerio.getPrecio();
        //return (precioPlan + precioRefrigerio) * participantes;
        return null;
    }

    public static Reserva create(Usuario usuario, Guia guia, Plan plan, Integer participantes, Refrigerio refrigerio, LocalDateTime fechaReserva, Integer precioTotal) {
        // Lógica para crear una nueva reserva
        ReservaId nuevoId = ReservaId.generate(); // Generar un nuevo ID
        //Integer precioTotal = calcularPrecioTotal(); // Calcular el precio total
        return new Reserva(nuevoId, usuario, guia, plan, participantes, refrigerio, fechaReserva, precioTotal);
    }

    public static Reserva reconstruct(ReservaId id, Usuario usuario, Guia guia, Plan plan, Integer participantes, Refrigerio refrigerio, LocalDateTime fechaReserva, EstadoReserva estado, Integer precioTotal) {
        // Lógica para reconstruir una reserva existente
        Reserva reserva = new Reserva(id, usuario, guia, plan, participantes, refrigerio, fechaReserva, precioTotal);
        reserva.estado = estado; // Establecer el estado existente
        return reserva;
    }

    public void actualizarReserva(Reserva reserva) {
        this.usuario = reserva.getUsuario();
        this.guia = reserva.getGuia();
        this.plan = reserva.getPlan();
        this.participantes = reserva.getParticipantes();
        this.refrigerio = reserva.getRefrigerio();
        this.fechaReserva = reserva.getFechaReserva();
        this.estado = reserva.getEstado();
        this.precioTotal = reserva.getPrecioTotal();
    }


    // Getters

    public ReservaId getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Guia getGuia() {
        return guia;
    }

    public Plan getPlan() {
        return plan;
    }

    public Integer getParticipantes() {
        return participantes;
    }

    public Refrigerio getRefrigerio() {
        return refrigerio;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public Integer getPrecioTotal() {
        return precioTotal;
    }
}
