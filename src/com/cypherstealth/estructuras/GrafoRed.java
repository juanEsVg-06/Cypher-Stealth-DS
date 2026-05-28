package com.cypherstealth.estructuras;

import com.cypherstealth.model.ActivoRed;
import java.util.*;

public class GrafoRed {

    private Map<ActivoRed, List<ActivoRed>> adyacencias;

    public GrafoRed() {
        // Inicialización obligatoria
        this.adyacencias = new HashMap<>();
    }

    public void agregarNodo(ActivoRed nodo) {
        adyacencias.putIfAbsent(nodo, new ArrayList<>());
    }

    public void agregarConexion(ActivoRed origen, ActivoRed destino) {
        // Aseguramos que ambos nodos existan antes de conectarlos
        agregarNodo(origen);
        agregarNodo(destino);
        adyacencias.get(origen).add(destino);
    }

    public List<ActivoRed> obtenerVecinos(ActivoRed nodo) {
        return adyacencias.getOrDefault(nodo, new ArrayList<>());
    }

    // Útil para listar todos los equipos en la interfaz gráfica
    public Set<ActivoRed> obtenerTodosLosNodos() {
        return adyacencias.keySet();
    }
}