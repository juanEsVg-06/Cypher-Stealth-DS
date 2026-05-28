package com.cypherstealth;

import com.cypherstealth.model.AnalistaSeguridad;
import java.util.LinkedList;
import java.util.List;

public class ModuloGestionMFA {

    private List<AnalistaSeguridad> analistas = new LinkedList<>();

    public void crearAnalista(AnalistaSeguridad a) {
        if (a != null) {
            analistas.add(a);
        }
    }

    public AnalistaSeguridad buscarAnalista(String id) {
        return analistas.stream()
                //Filtra asegurando que el ID coincida
                .filter(a -> a != null && a.getIdAnalista().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean validarToken(AnalistaSeguridad a, String token) {
        return a != null && a.autenticar(token);
    }

    // ELIMINAR (D): Para remover analistas inactivos y completar el CRUD
    public void eliminarAnalista(String id) {
        analistas.removeIf(a -> a != null && a.getIdAnalista().equals(id));
    }
}