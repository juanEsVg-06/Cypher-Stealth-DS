package com.cypherstealth.estructuras;

import com.cypherstealth.model.EventoForense;
import java.util.*;
public class Pila {

    private Stack<EventoForense> pila;

    public Pila() {
        // Inicialización obligatoria
        this.pila = new Stack<>();
    }

    public void push(EventoForense e) {
        pila.push(e);
    }

    public EventoForense pop() {
        if (!pila.isEmpty()) {
            return pila.pop();
        }
        return null;
    }

    public EventoForense peek() {
        if (!pila.isEmpty()) {
            return pila.peek();
        }
        return null;
    }

    public boolean estaVacia() {
        return pila.isEmpty();
    }

    // Método para mostrar el historial en la interfaz (LIFO)
    public List<EventoForense> obtenerHistorialVista() {
        List<EventoForense> vista = new ArrayList<>(pila);
        // Invertimos la lista para mostrar el último evento arriba
        java.util.Collections.reverse(vista);
        return vista;
    }
}