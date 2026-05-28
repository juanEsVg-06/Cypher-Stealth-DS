package com.cypherstealth.estructuras;

import com.cypherstealth.model.TicketIncidente;
import java.util.*;

public class ColaPrioridad {

    private PriorityQueue<TicketIncidente> cola;

    public ColaPrioridad() {
        // Inicialización obligatoria
        this.cola = new PriorityQueue<>();
    }

    public void insertar(TicketIncidente t) {
        cola.add(t);
    }

    public TicketIncidente extraerMaximo() {
        return cola.poll();
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }

    // Útil para mostrar la cola entera en una tabla de la interfaz gráfica sin vaciarla
    public List<TicketIncidente> obtenerTodosParaVista() {
        return new ArrayList<>(cola);
    }
}