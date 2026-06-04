package com.cypherstealth.model;

import java.util.Date;

// Implementamos Comparable para facilitar su ordenamiento en la ColaPrioridad
public class TicketIncidente implements Comparable<TicketIncidente> {
    private String idTicket;
    private int severidad;
    private Estado estado;
    private String descripcion;
    private Date fechaCreacion;

    // Relaciones según el UML
    private Alerta alertaOrigen;
    private AnalistaSeguridad analistaAsignado;
    private ActivoRed activoAfectado;

    public TicketIncidente(String idTicket, Estado estado, Alerta alertaOrigen, ActivoRed activoAfectado, String descripcion) {
        this.idTicket = idTicket;
        this.alertaOrigen = alertaOrigen;
        this.activoAfectado = activoAfectado;
        this.severidad = alertaOrigen.calcularSeveridad();
        this.estado = estado;
        this.descripcion = descripcion; // Ahora sí lo guardamos
        this.fechaCreacion = new Date();
    }

    // Método definido en el UML
    public int calcularPrioridad() {
        // Aquí podemos añadir lógica extra, ej: si el activoAfectado es crítico, subir la prioridad
        int prioridadFinal = severidad;
        if (activoAfectado != null && activoAfectado.esCritico()) {
            prioridadFinal += 20; // Bonus de prioridad para servidores críticos
        }
        return prioridadFinal;
    }

    public String getDescripcion() { return descripcion; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public ActivoRed getActivoAfectado() { return activoAfectado; }
    public Alerta getAlertaOrigen() { return alertaOrigen; }

    // Lógica para comparar tickets y ordenarlos por mayor prioridad (Heapsort / Priority Queue)
    @Override
    public int compareTo(TicketIncidente otroTicket) {
        // Orden descendente: el de mayor prioridad va primero
        return Integer.compare(otroTicket.calcularPrioridad(), this.calcularPrioridad());
    }

    // Getters y Setters...
    public String getIdTicket() { return idTicket; }

    public Estado getEstado() {
        return estado;
    }
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getSeveridad() { return severidad; }
}