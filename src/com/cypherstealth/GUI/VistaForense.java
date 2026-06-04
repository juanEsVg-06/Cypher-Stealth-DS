package com.cypherstealth.GUI;

import com.cypherstealth.ModuloForenseTimeline;
import com.cypherstealth.model.EventoForense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class VistaForense extends VBox {

    private ModuloForenseTimeline moduloForense;
    private ListView<String> listaVisual;
    private ObservableList<String> itemsVista;

    public VistaForense(ModuloForenseTimeline moduloForense) {
        this.moduloForense = moduloForense;
        this.itemsVista = FXCollections.observableArrayList();
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        this.setPadding(new Insets(20));
        this.setSpacing(15);
        this.setStyle("-fx-background-color: #121216;"); // Fondo oscuro

        Label lblTitulo = new Label("TIMELINE FORENSE (LIFO)");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #5e2c8f; -fx-font-family: 'Consolas';");

        // Lista visual simulando una terminal hacker
        listaVisual = new ListView<>();
        listaVisual.setItems(itemsVista);
        listaVisual.setPrefHeight(300);
        // Aplicamos el verde neón y el fondo negro a la lista
        listaVisual.setStyle("-fx-control-inner-background: #000000; -fx-text-fill: #39ff14; -fx-font-family: 'Consolas'; -fx-border-color: #5e2c8f; -fx-border-width: 2px;");

        // Estilo base para los botones
        String estiloBotones = "-fx-background-color: #2a2a35; -fx-text-fill: #cda434; -fx-font-weight: bold; -fx-border-color: #cda434; -fx-border-radius: 3px; -fx-cursor: hand;";

        Button btnActualizar = new Button("Refrescar Log");
        btnActualizar.setStyle(estiloBotones);

        Button btnPop = new Button("Extraer (POP)");
        btnPop.setStyle(estiloBotones);

        Button btnPurgar = new Button("Purgar Memoria");
        btnPurgar.setStyle("-fx-background-color: #3a1111; -fx-text-fill: #ff3333; -fx-font-weight: bold; -fx-border-color: #ff3333; -fx-cursor: hand;");

        HBox cajaBotones = new HBox(15, btnActualizar, btnPop, btnPurgar);

        // Lógica de los botones
        btnActualizar.setOnAction(e -> actualizarLista());

        btnPop.setOnAction(e -> {
            EventoForense extraido = moduloForense.extraerUltimoEvento();
            if (extraido != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Evento extraído: " + extraido.getDescripcion());
                // Aplicar estilo rápido a la alerta nativa es complejo en JavaFX, la dejamos estándar
                alert.show();
                actualizarLista();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Stack de eventos vacío.");
                alert.show();
            }
        });

        btnPurgar.setOnAction(e -> {
            moduloForense.eliminarAllHistorial();
            actualizarLista();
        });

        this.getChildren().addAll(lblTitulo, listaVisual, cajaBotones);
    }

    public void actualizarLista() {
        itemsVista.clear();
        List<EventoForense> eventos = moduloForense.verLineaDeTiempo();
        for (EventoForense e : eventos) {
            // Formateo tipo terminal
            itemsVista.add("root@cypher-stealth:~# [" + e.getTimestamp().toString() + "] " + e.getDescripcion());
        }
    }
}