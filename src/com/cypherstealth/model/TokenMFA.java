package com.cypherstealth.model;

public class TokenMFA {
    private String idToken;
    private String claveCriptografica;
    private EstadoToken estado; // Ahora usa tu nuevo Enum

    public TokenMFA(String idToken, String claveCriptografica, EstadoToken estado) {
        this.idToken = idToken;
        this.claveCriptografica = claveCriptografica;
        this.estado = estado;
    }

    // Lógica para validar si el token ingresado coincide y su estado es ACTIVO
    public boolean esValido(String tokenIngresado) {
        // Con los Enums usamos '==' en lugar de '.equals()' para comparar estados
        return this.claveCriptografica.equals(tokenIngresado) && this.estado == EstadoToken.ACTIVO;
    }
}