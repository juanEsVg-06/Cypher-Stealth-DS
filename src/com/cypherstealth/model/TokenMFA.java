package com.cypherstealth.model;

public class TokenMFA {
    private String idToken;
    private String claveCriptografica;
    private EstadoToken estado; // Podría ser otro Enum (ACTIVO, REVOCADO, PERDIDO)

    public TokenMFA(String idToken, String claveCriptografica, EstadoToken estado) {
        this.idToken = idToken;
        this.claveCriptografica = claveCriptografica;
        this.estado = estado;
    }

    // Lógica para validar si el token ingresado coincide con el hardware
    public boolean esValido(String tokenIngresado) {
        return this.claveCriptografica.equals(tokenIngresado) && this.estado.equals(EstadoToken.ACTIVO);
    }
}