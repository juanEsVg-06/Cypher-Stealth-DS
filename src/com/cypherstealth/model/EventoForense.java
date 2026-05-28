package com.cypherstealth.model;

import java.util.Date;

public class EventoForense {
    private String idEvento;
    private String tipo;
    private String descripcion;
    private Date timestamp;
    private String notasTecnicas;
    private TicketIncidente ticketRelacionado;

    public EventoForense(String idEvento, String tipo, String descripcion, TicketIncidente ticket) {
        this.idEvento = idEvento;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.ticketRelacionado = ticket;
        this.timestamp = new Date();
        this.notasTecnicas = "";
    }

    // Método para el CRUD (Actualizar)
    public void agregarNota(String nota) {
        this.notasTecnicas += "\n- " + nota;
    }

    // Getters
    public String getDescripcion() { return descripcion; }
    public String getNotasTecnicas() { return notasTecnicas; }
    public Date getTimestamp() { return timestamp; }
}