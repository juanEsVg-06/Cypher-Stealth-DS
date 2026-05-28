package com.cypherstealth.model;

import java.util.Date;

public class Alerta {
    private String idAlerta;
    private String tipo;
    private int vulnerabilidad;
    private int impacto;
    private Date timestamp;

    public Alerta(String idAlerta, String tipo, int vulnerabilidad, int impacto) {
        this.idAlerta = idAlerta;
        this.tipo = tipo;
        this.vulnerabilidad = vulnerabilidad;
        this.impacto = impacto;
        this.timestamp = new Date();
    }

    // El UML especifica este método: S = Vulnerabilidad x Impacto
    public int calcularSeveridad() {
        return this.vulnerabilidad * this.impacto;
    }

    // Getters y Setters
    public String getIdAlerta() { return idAlerta; }
    public String getTipo() { return tipo; }
    public Date getTimestamp() { return timestamp; }
}