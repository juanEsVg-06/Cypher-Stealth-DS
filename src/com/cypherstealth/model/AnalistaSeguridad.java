package com.cypherstealth.model;

public class AnalistaSeguridad {
    private String idAnalista;
    private String nombre;
    private String email;
    private String nivelPrivilegio; // ej. "Tier 1", "Tier 2", "Admin"
    private TokenMFA tokenMFA; // Relación directa con su llave física

    public AnalistaSeguridad(String idAnalista, String nombre, String email, String nivelPrivilegio, TokenMFA tokenMFA) {
        this.idAnalista = idAnalista;
        this.nombre = nombre;
        this.email = email;
        this.nivelPrivilegio = nivelPrivilegio;
        this.tokenMFA = tokenMFA;
    }

    // Método de autenticación definido en tu UML
    public boolean autenticar(String tokenIngresado) {
        if (this.tokenMFA != null) {
            return this.tokenMFA.esValido(tokenIngresado);
        }
        return false;
    }

    // Getters
    public String getIdAnalista() { return idAnalista; }
    public String getNombre() { return nombre; }
    public String getNivelPrivilegio() { return nivelPrivilegio; }
}