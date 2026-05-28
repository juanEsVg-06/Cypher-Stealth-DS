package com.cypherstealth;

import com.cypherstealth.model.EventoForense;
import com.cypherstealth.estructuras.Pila;
import java.util.List;

public class ModuloForenseTimeline {

    private Pila pilaEventos;

    public ModuloForenseTimeline() {
        // Inicialización segura
        this.pilaEventos = new Pila();
    }

    //CREAR (C): Apila un nuevo log de evento crítico dentro de la línea de tiempo
    public void registrarEvento(EventoForense e) {
        if (e != null) {
            pilaEventos.push(e);
        }
    }

    /*LEER (R) - Backtracking: Extrae y elimina el último evento registrado
    para reconstruir la cadena de ataque de forma inversa (LIFO) */
    public EventoForense extraerUltimoEvento() {
        return pilaEventos.pop();
    }

    /*LEER (R) - Interfaz Gráfica: Obtiene toda la línea de tiempo para mostrarla
     en un JTable o JList, sin eliminar los eventos de la pila */
    public List<EventoForense> verLineaDeTiempo() {
        return pilaEventos.obtenerHistorialVista();
    }

    //ACTUALIZAR (U): Añade notas técnicas o indicadores de compromiso
    public void actualizarEventoForense(EventoForense evento, String nota) {
        if (evento != null && nota != null && !nota.isEmpty()) {
            evento.agregarNota(nota);
        }
    }

    /*ELIMINAR (D): Purga el historial de logs temporales una vez que
    el ticket del incidente ha sido cerrado definitivamente */
    public void eliminarAllHistorial() {
        this.pilaEventos = new Pila();
    }
}