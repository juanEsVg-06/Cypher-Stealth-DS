package com.cypherstealth;

import com.cypherstealth.model.*;
import com.cypherstealth.estructuras.ColaPrioridad;
import java.util.UUID;
import java.util.List;

public class ModuloTriageAlertas {

    // Instanciamos la estructura de datos core
    private ColaPrioridad colaTickets;

    public ModuloTriageAlertas() {
        this.colaTickets = new ColaPrioridad();
    }

    public TicketIncidente crearTicket(Alerta alerta, AnalistaSeguridad analista, ActivoRed activoAfectado, String descripcion) {
        // 1. Generacion de ID unico
        String idTicket = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Pasamos la descripción dinámica en lugar del texto quemado
        // 2. Creacion del objeto para el manejo de tickets
        TicketIncidente nuevoTicket = new TicketIncidente(idTicket, Estado.PENDIENTE, alerta, activoAfectado, descripcion);

        // 3. PriorityQueue lo ordena automáticamente
        colaTickets.insertar(nuevoTicket);
        return nuevoTicket;
    }

    //LEER (R): Extrae el incidente más crítico para ser atendido inmediatamente.
    public TicketIncidente obtenerSiguienteCritico() {
        // Extrae y remueve el ticket con mayor prioridad de la cola
        return colaTickets.extraerMaximo();
    }

     //LEER (R): Visualizar la cola de incidentes pendientes
    public List<TicketIncidente> verTicketsPendientes() {
        return colaTickets.obtenerTodosParaVista();
    }

    //ACTUALIZAR (U): Cambiar el estado del ticket[cite: 60].
    public void actualizarEstadoTicket(TicketIncidente ticket, Estado nuevoEstado) {
        if (ticket != null) {
            ticket.setEstado(nuevoEstado);
        }
    }

    // ELIMINAR (D): Descartar y borrar alertas confirmadas como falsos positivos[cite: 61].
    public void descartarFalsoPositivo(TicketIncidente ticket) {
        if (ticket != null) {
            // En lugar de hacer una búsqueda costosa O(n) en la cola para borrarlo,
            // simplemente lo marcamos como resuelto para limpiar el flujo de trabajo.
            ticket.setEstado(Estado.RESUELTO);
        }
    }
}