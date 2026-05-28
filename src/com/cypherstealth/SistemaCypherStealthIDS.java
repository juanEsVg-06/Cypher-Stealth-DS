package com.cypherstealth;

public class SistemaCypherStealthIDS {

    private ModuloGestionMFA mfa;
    private ModuloTriageAlertas triage;
    private ModuloForenseTimeline forense;
    private ModuloInventarioActivos inventario;

    public SistemaCypherStealthIDS() {
        mfa = new ModuloGestionMFA();
        triage = new ModuloTriageAlertas();
        forense = new ModuloForenseTimeline();
        inventario = new ModuloInventarioActivos();
    }

    public void iniciar() {
        System.out.println("Sistema Cypher Stealth-DS iniciado de forma segura... ");
    }

    public void detener() {
        System.out.println("Sistema detenido.");
    }

    // --- GETTERS PARA LA INTERFAZ GRÁFICA ---

    public ModuloGestionMFA getMfa() {
        return mfa;
    }

    public ModuloTriageAlertas getTriage() {
        return triage;
    }

    public ModuloForenseTimeline getForense() {
        return forense;
    }

    public ModuloInventarioActivos getInventario() {
        return inventario;
    }
}