package com.cypherstealth.GUI;

import com.cypherstealth.ModuloTriageAlertas;
import com.cypherstealth.model.ActivoRed;
import com.cypherstealth.model.Alerta;
import com.cypherstealth.model.TicketIncidente;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.UUID;

public class VistaTriage extends HBox {

    private ModuloTriageAlertas moduloTriage;
    private TableView<TicketIncidente> tablaTickets;

    public VistaTriage(ModuloTriageAlertas moduloTriage) {
        this.moduloTriage = moduloTriage;
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        this.setStyle("-fx-background-color: #121216;");
        this.setPadding(new Insets(20));
        this.setSpacing(20);

        String estiloCampos = "-fx-background-color: #1e1e24; -fx-text-fill: #39ff14; -fx-border-color: #5e2c8f; -fx-border-width: 1px;";

        // ==========================================
        // PANEL IZQUIERDO: FORMULARIO DE INGRESO
        // ==========================================
        VBox panelControles = new VBox(12);
        panelControles.setPrefWidth(280);

        Label lblTitulo = new Label("GESTOR DE ALERTAS");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #cda434; -fx-font-family: 'Consolas';");

        TextField txtTipo = new TextField(); txtTipo.setPromptText("Tipo (ej. Fuerza Bruta)"); txtTipo.setStyle(estiloCampos);
        TextField txtVuln = new TextField(); txtVuln.setPromptText("Vulnerabilidad (1-10)"); txtVuln.setStyle(estiloCampos);
        TextField txtImpacto = new TextField(); txtImpacto.setPromptText("Impacto (1-10)"); txtImpacto.setStyle(estiloCampos);

        TextArea txtDesc = new TextArea();
        txtDesc.setPromptText("Descripción del incidente...");
        txtDesc.setPrefRowCount(3);
        txtDesc.setStyle("-fx-control-inner-background: #1e1e24; -fx-text-fill: #39ff14; -fx-border-color: #5e2c8f;");

        Label lblActivo = new Label("Activo Afectado:"); lblActivo.setStyle("-fx-text-fill: #888;");
        TextField txtActivoId = new TextField(); txtActivoId.setPromptText("ID Equipo (ej. SRV-01)"); txtActivoId.setStyle(estiloCampos);
        TextField txtActivoCrit = new TextField(); txtActivoCrit.setPromptText("Criticidad Equipo (1-10)"); txtActivoCrit.setStyle(estiloCampos);

        Button btnCrearTicket = new Button("Generar Ticket (Insertar en Heap)");
        btnCrearTicket.setPrefWidth(Double.MAX_VALUE);
        btnCrearTicket.setStyle("-fx-background-color: #5e2c8f; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        Button btnAtender = new Button("Atender Max Prioridad (Pop Heap)");
        btnAtender.setPrefWidth(Double.MAX_VALUE);
        btnAtender.setStyle("-fx-background-color: #2a2a35; -fx-text-fill: #cda434; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: #cda434;");

        panelControles.getChildren().addAll(
                lblTitulo, new Label("Detalles de Alerta:"), txtTipo, txtVuln, txtImpacto, txtDesc,
                new Separator(), lblActivo, txtActivoId, txtActivoCrit,
                new Separator(), btnCrearTicket, btnAtender
        );

        // ==========================================
        // PANEL DERECHO: TABLA (PRIORITY QUEUE)
        // ==========================================
        VBox panelTabla = new VBox(10);
        HBox.setHgrow(panelTabla, Priority.ALWAYS);

        Label lblTabla = new Label("COLA DE PRIORIDAD (Tickets Pendientes)");
        lblTabla.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #5e2c8f; -fx-font-family: 'Consolas';");

        tablaTickets = new TableView<>();
        tablaTickets.setStyle("-fx-base: #1e1e24; -fx-control-inner-background: #1e1e24; -fx-table-cell-border-color: #5e2c8f; -fx-text-background-color: #39ff14; -fx-font-family: 'Consolas';");
        VBox.setVgrow(tablaTickets, Priority.ALWAYS);

        TableColumn<TicketIncidente, String> colId = new TableColumn<>("ID Ticket");
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdTicket()));
        colId.setPrefWidth(100);

        TableColumn<TicketIncidente, String> colPrioridad = new TableColumn<>("Prioridad");
        colPrioridad.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().calcularPrioridad())));
        colPrioridad.setPrefWidth(80);

        TableColumn<TicketIncidente, String> colAlerta = new TableColumn<>("Tipo Alerta");
        colAlerta.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAlertaOrigen().getTipo()));
        colAlerta.setPrefWidth(130);

        TableColumn<TicketIncidente, String> colDesc = new TableColumn<>("Descripción");
        colDesc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));
        colDesc.setPrefWidth(220);

        tablaTickets.getColumns().addAll(colId, colPrioridad, colAlerta, colDesc);

        panelTabla.getChildren().addAll(lblTabla, tablaTickets);

        // --- LÓGICA DE EVENTOS ---
        btnCrearTicket.setOnAction(e -> {
            try {
                // Instanciación basada en los inputs reales del usuario
                String idAlerta = "ALR-" + UUID.randomUUID().toString().substring(0, 4);
                Alerta nuevaAlerta = new Alerta(idAlerta, txtTipo.getText(), Integer.parseInt(txtVuln.getText()), Integer.parseInt(txtImpacto.getText()));

                ActivoRed activo = new ActivoRed(txtActivoId.getText(), "0.0.0.0", "MAC", Integer.parseInt(txtActivoCrit.getText()));

                moduloTriage.crearTicket(nuevaAlerta, null, activo, txtDesc.getText());
                actualizarTabla();

                txtTipo.clear(); txtVuln.clear(); txtImpacto.clear(); txtDesc.clear(); txtActivoId.clear(); txtActivoCrit.clear();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, ingresa números válidos en Vulnerabilidad, Impacto y Criticidad.");
                alert.show();
            }
        });

        btnAtender.setOnAction(e -> {
            TicketIncidente critico = moduloTriage.obtenerSiguienteCritico();
            if (critico != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ticket Atendido y Removido de la Cola:\nID: " + critico.getIdTicket() + "\nDescripción: " + critico.getDescripcion());
                alert.show();
                actualizarTabla();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No hay tickets pendientes en la cola.");
                alert.show();
            }
        });

        this.getChildren().addAll(panelControles, panelTabla);
    }

    public void actualizarTabla() {
        List<TicketIncidente> pendientes = moduloTriage.verTicketsPendientes();
        tablaTickets.setItems(FXCollections.observableArrayList(pendientes));
        tablaTickets.getItems().sort(TicketIncidente::compareTo);
    }
}