package com.cypherstealth.model;

public class ActivoRed {
    private String idActivo;
    private String ip;
    private String mac;
    private int nivelImportancia;

    public ActivoRed(String idActivo, String ip, String mac, int nivelImportancia) {
        this.idActivo = idActivo;
        this.ip = ip;
        this.mac = mac;
        this.nivelImportancia = nivelImportancia;
    }

    // Este es el método que llama TicketIncidente para saber si dar el bono de prioridad
    public boolean esCritico() {
        // Consideramos crítico si nivel de importancia es 8 o superior
        return this.nivelImportancia >= 8;
    }

    // Getters y Setters
    public String getIdActivo() { return idActivo; }
    public String getIp() { return ip; }
    public String getMac() { return mac; }
    public int getNivelImportancia() { return nivelImportancia; }
}