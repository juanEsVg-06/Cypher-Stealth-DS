package com.cypherstealth.estructuras;

import com.cypherstealth.model.ActivoRed;
import java.util.*;

public class GrafoRed {

    private Map<ActivoRed, List<ActivoRed>> adyacencias;

    public GrafoRed() {
        this.adyacencias = new HashMap<>();
    }

    public void agregarNodo(ActivoRed nodo) {
        adyacencias.putIfAbsent(nodo, new ArrayList<>());
    }

    public void agregarConexion(ActivoRed origen, ActivoRed destino) {
        // Aseguramos que ambos nodos existan en el mapa
        agregarNodo(origen);
        agregarNodo(destino);

        // Creamos la conexión de Ida (Origen -> Destino)
        adyacencias.get(origen).add(destino);

        // ¡NUEVO! Creamos la conexión de Vuelta (Destino -> Origen)
        // Esto hace que el grafo sea no dirigido, simulando un cable de red real (Packet Tracer style)
        adyacencias.get(destino).add(origen);
    }

    public List<ActivoRed> obtenerVecinos(ActivoRed nodo) {
        return adyacencias.getOrDefault(nodo, new ArrayList<>());
    }

    public Set<ActivoRed> obtenerTodosLosNodos() {
        return adyacencias.keySet();
    }
}