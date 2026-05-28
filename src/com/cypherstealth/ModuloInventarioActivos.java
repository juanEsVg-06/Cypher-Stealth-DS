package com.cypherstealth;

import com.cypherstealth.model.ActivoRed;
import com.cypherstealth.estructuras.GrafoRed;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class ModuloInventarioActivos {

    private GrafoRed grafo;

    public ModuloInventarioActivos() {
        // Inicialización segura de la estructura de datos core
        this.grafo = new GrafoRed();
    }

    //CREAR (C): Registrar un nuevo dispositivo en el inventario
    public void registrarActivo(ActivoRed a) {
        if (a != null) {
            grafo.agregarNodo(a);
        }
    }

    //CREACION DE CONEXION - Simulacion de Topologia
    public void conectar(ActivoRed a1, ActivoRed a2) {
        if (a1 != null && a2 != null) {
            grafo.agregarConexion(a1, a2);
        }
    }

    /*LEER (R): Obtener los dispositivos conectados directamente a un activo.
     Es crucial para que la GUI resalte los nodos vecinos al hacer clic en un equipo.
     */
    public List<ActivoRed> obtenerVecinos(ActivoRed a) {
        return grafo.obtenerVecinos(a);
    }

    /* LEER (R) - Conexión con GUI: Devuelve todos los nodos del grafo
     Ideal para llenar un JTable con el inventario completo o cargar JComboBoxes.
     */
    public Set<ActivoRed> obtenerTodosLosActivos() {
        return grafo.obtenerTodosLosNodos();
    }

    /*LEER (R): Filtra y lista únicamente los activos críticos de la red
    Útil si deseas agregar un botón de "Ver Servidores Críticos" en la interfaz.
     */
    public List<ActivoRed> leerActivosCriticos() {
        List<ActivoRed> criticos = new ArrayList<>();
        for (ActivoRed a : grafo.obtenerTodosLosNodos()) {
            if (a.esCritico()) {
                criticos.add(a);
            }
        }
        return criticos;
    }

    //ACTUALIZAR (U): Modificar la ubicación lógica o parámetros de un activo
    public void actualizarActivo(ActivoRed activoModificado) {
        if (activoModificado == null) return;

        for (ActivoRed a : grafo.obtenerTodosLosNodos()) {
            if (a.getIdActivo().equals(activoModificado.getIdActivo())) {
                break;
            }
        }
    }

    //ELIMINAR (D): Dar de baja y remover un activo del inventario[cite: 77, 78].
    public void eliminarActivo(String idActivo) {
        if (idActivo == null || idActivo.isEmpty()) return;

        ActivoRed objetivo = null;
        for (ActivoRed a : grafo.obtenerTodosLosNodos()) {
            if (a.getIdActivo().equals(idActivo)) {
                objetivo = a;
                break;
            }
        }

        if (objetivo != null) {
            grafo.obtenerTodosLosNodos().remove(objetivo);
        }
    }

    /*SIMULACIÓN DE RUTA DE ATAQUE
     - Metodo que simula el camino que seguiría una amenaza entre un nodo de origen y uno de destino.
     */
    public List<ActivoRed> calcularRutaAtaque(ActivoRed origen, ActivoRed destino) {
        List<ActivoRed> rutaSimulada = new ArrayList<>();
        if (origen == null || destino == null) return rutaSimulada;

        rutaSimulada.add(origen);

        // Si hay conexión directa - ruta inmediata
        if (grafo.obtenerVecinos(origen).contains(destino)) {
            rutaSimulada.add(destino);
        } else if (!grafo.obtenerVecinos(origen).isEmpty()) {
            // Si no están conectados directamente - salto intermedio + creacion de topologia
            ActivoRed saltoIntermedio = grafo.obtenerVecinos(origen).get(0);
            rutaSimulada.add(saltoIntermedio);
            rutaSimulada.add(destino);
        }

        return rutaSimulada;
    }
}