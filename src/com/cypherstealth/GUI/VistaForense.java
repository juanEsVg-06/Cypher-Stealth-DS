package com.cypherstealth.GUI;

import com.cypherstealth.ModuloForenseTimeline;
import com.cypherstealth.model.EventoForense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.UUID;

public class VistaForense extends HBox {

    private ModuloForenseTimeline moduloForense;
    private ListView<String> listaVisual;
    private ObservableList<String> itemsVista;

    public VistaForense(ModuloForenseTimeline moduloForense) {
        this.moduloForense = moduloForense;
        this.itemsVista = FXCollections.observableArrayList();
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        this.setStyle("-fx-background-color: #121216;");
        this.setPadding(new Insets(20));
        this.setSpacing(20);

        String estiloCampos = "-fx-background-color: #1e1e24; -fx-text-fill: #39ff14; -fx-border-color: #5e2c8f; -fx-border-width: 1px;";

        // ==========================================
        // PANEL IZQUIERDO: INPUT DE LOGS
        // ==========================================
        VBox panelInputs = new VBox(15);
        panelInputs.setPrefWidth(280);

        Label lblTitulo = new Label("AUDITORÍA DE EVENTOS");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #cda434; -fx-font-family: 'Consolas';");

        TextField txtTipo = new TextField(); txtTipo.setPromptText("Tipo de Evento (ej. Malware, Login Falso)");
        txtTipo.setStyle(estiloCampos);

        TextArea txtDesc = new TextArea(); txtDesc.setPromptText("Descripción detallada o IoCs...");
        txtDesc.setPrefRowCount(4);
        txtDesc.setStyle("-fx-control-inner-background: #1e1e24; -fx-text-fill: #39ff14; -fx-border-color: #5e2c8f;");

        Button btnPush = new Button("Apilar Evento (PUSH)");
        btnPush.setPrefWidth(Double.MAX_VALUE);
        btnPush.setStyle("-fx-background-color: #5e2c8f; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        Button btnPop = new Button("Extraer Último (POP)");
        btnPop.setPrefWidth(Double.MAX_VALUE);
        btnPop.setStyle("-fx-background-color: #2a2a35; -fx-text-fill: #cda434; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: #cda434;");

        Button btnPurgar = new Button("Purgar Memoria");
        btnPurgar.setPrefWidth(Double.MAX_VALUE);
        btnPurgar.setStyle("-fx-background-color: #3a1111; -fx-text-fill: #ff3333; -fx-font-weight: bold; -fx-border-color: #ff3333; -fx-cursor: hand;");

        panelInputs.getChildren().addAll(lblTitulo, new Label("Nuevo Log:"), txtTipo, txtDesc, btnPush, new Separator(), btnPop, btnPurgar);

        // ==========================================
        // PANEL DERECHO: TERMINAL LIFO
        // ==========================================
        VBox panelTerminal = new VBox(10);
        HBox.setHgrow(panelTerminal, Priority.ALWAYS);

        Label lblTerminal = new Label("TIMELINE FORENSE (LIFO STACK)");
        lblTerminal.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #5e2c8f; -fx-font-family: 'Consolas';");

        listaVisual = new ListView<>();
        listaVisual.setItems(itemsVista);
        VBox.setVgrow(listaVisual, Priority.ALWAYS);
        listaVisual.setStyle("-fx-control-inner-background: #000000; -fx-text-fill: #39ff14; -fx-font-family: 'Consolas'; -fx-border-color: #5e2c8f; -fx-border-width: 2px;");

        panelTerminal.getChildren().addAll(lblTerminal, listaVisual);

        // --- LÓGICA DE EVENTOS ---
        btnPush.setOnAction(e -> {
            if (!txtTipo.getText().isEmpty() && !txtDesc.getText().isEmpty()) {
                String idEvento = "EVT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                // Ticket null porque el evento se está metiendo manual sin ticket asociado
                EventoForense nuevoEvento = new EventoForense(idEvento, txtTipo.getText(), txtDesc.getText(), null);
                moduloForense.registrarEvento(nuevoEvento);
                actualizarLista();
                txtTipo.clear(); txtDesc.clear();
            }
        });

        btnPop.setOnAction(e -> {
            EventoForense extraido = moduloForense.extraerUltimoEvento();
            if (extraido != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Evento Extraído (Backtracking):\n[" + extraido.getTimestamp() + "] " + extraido.getDescripcion());
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

        this.getChildren().addAll(panelInputs, panelTerminal);
    }

    public void actualizarLista() {
        itemsVista.clear();
        List<EventoForense> eventos = moduloForense.verLineaDeTiempo();
        for (EventoForense e : eventos) {
            itemsVista.add("root@cypher-stealth:~# [" + e.getTimestamp().toString() + "] " + e.getDescripcion());
        }
    }
}