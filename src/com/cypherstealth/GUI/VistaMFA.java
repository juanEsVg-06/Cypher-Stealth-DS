package com.cypherstealth.GUI;

import com.cypherstealth.ModuloGestionMFA;
import com.cypherstealth.model.AnalistaSeguridad;
import com.cypherstealth.model.TokenMFA;
import com.cypherstealth.model.EstadoToken;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.io.File;
import java.util.UUID;
import java.util.regex.Pattern;

public class VistaMFA extends HBox {

    private ModuloGestionMFA moduloMFA;

    // Referencias a las pestañas controladas
    private Tab tabTriage;
    private Tab tabForense;
    private Tab tabInventario;
    private TabPane tabPane;

    public VistaMFA(ModuloGestionMFA moduloMFA, Tab tabTriage, Tab tabForense, Tab tabInventario, TabPane tabPane) {
        this.moduloMFA = moduloMFA;
        this.tabTriage = tabTriage;
        this.tabForense = tabForense;
        this.tabInventario = tabInventario;
        this.tabPane = tabPane;
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        this.setStyle("-fx-background-color: #121216;");
        this.setPadding(new Insets(30));
        this.setSpacing(40);
        this.setAlignment(Pos.CENTER);

        String estiloCampos = "-fx-background-color: #1e1e24; -fx-text-fill: #39ff14; -fx-border-color: #5e2c8f; -fx-border-width: 1px;";
        String estiloBoton = "-fx-background-color: #5e2c8f; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: #cda434;";

        // ==========================================
        // PANEL IZQUIERDO: LOGIN
        // ==========================================
        VBox panelLogin = new VBox(15);
        panelLogin.setAlignment(Pos.CENTER);
        panelLogin.setPrefWidth(350);
        panelLogin.setStyle("-fx-border-color: #333; -fx-border-width: 1px; -fx-padding: 20px;");

        ImageView imgLogo = new ImageView();
        try {
            File file = new File("logo.png");
            imgLogo.setImage(new Image(file.toURI().toString()));
            imgLogo.setFitWidth(100); imgLogo.setPreserveRatio(true);
        } catch (Exception ignored) {}

        Label lblLogin = new Label("BÓVEDA MFA - ACCESO");
        lblLogin.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #cda434; -fx-font-family: 'Consolas';");

        TextField txtIdLogin = new TextField(); txtIdLogin.setPromptText("ID del Analista"); txtIdLogin.setStyle(estiloCampos);
        PasswordField txtTokenLogin = new PasswordField(); txtTokenLogin.setPromptText("Token Físico (MFA)"); txtTokenLogin.setStyle(estiloCampos);

        Button btnLogin = new Button("AUTENTICAR");
        btnLogin.setPrefWidth(Double.MAX_VALUE);
        btnLogin.setStyle(estiloBoton);

        Label lblMensajeLogin = new Label();
        lblMensajeLogin.setStyle("-fx-font-family: 'Consolas'; -fx-font-weight: bold;");

        btnLogin.setOnAction(e -> {
            String id = txtIdLogin.getText();
            String token = txtTokenLogin.getText();

            // Aseguramos que todo se bloquee antes de verificar credenciales nuevas
            tabTriage.setDisable(true);
            tabForense.setDisable(true);
            tabInventario.setDisable(true);

            AnalistaSeguridad analista = moduloMFA.buscarAnalista(id);
            if (analista != null) {
                if (moduloMFA.validarToken(analista, token)) {
                    String nivel = analista.getNivelPrivilegio();
                    lblMensajeLogin.setText("> ACCESO CONCEDIDO: [" + nivel + "]");
                    lblMensajeLogin.setStyle("-fx-text-fill: #39ff14;");

                    // ---------------------------------------------------------
                    // SISTEMA DE ROLES (RBAC) - PRINCIPIO DE MÍNIMO PRIVILEGIO
                    // ---------------------------------------------------------
                    if (nivel.equals("Admin SOC")) {
                        tabTriage.setDisable(false);
                        tabForense.setDisable(false);
                        tabInventario.setDisable(false);
                    } else if (nivel.equals("Tier 2")) {
                        tabTriage.setDisable(false);
                        tabForense.setDisable(false);
                    } else if (nivel.equals("Tier 1")) {
                        tabTriage.setDisable(false);
                    }

                    // Forzar a la interfaz gráfica a saltar directamente al Triage tras loguearse
                    tabPane.getSelectionModel().select(tabTriage);
                    txtIdLogin.clear(); txtTokenLogin.clear();

                } else {
                    lblMensajeLogin.setText("> ERROR: TOKEN INVÁLIDO");
                    lblMensajeLogin.setStyle("-fx-text-fill: #ff3333;");
                }
            } else {
                lblMensajeLogin.setText("> ERROR: ANALISTA NO EXISTE");
                lblMensajeLogin.setStyle("-fx-text-fill: #ff3333;");
            }
        });

        panelLogin.getChildren().addAll(imgLogo, lblLogin, txtIdLogin, txtTokenLogin, btnLogin, lblMensajeLogin);

        // ==========================================
        // PANEL DERECHO: REGISTRO DE ANALISTAS
        // ==========================================
        VBox panelRegistro = new VBox(15);
        panelRegistro.setPrefWidth(350);
        panelRegistro.setStyle("-fx-border-color: #333; -fx-border-width: 1px; -fx-padding: 20px;");
        HBox.setHgrow(panelRegistro, Priority.ALWAYS);

        Label lblRegistro = new Label("ALTA DE NUEVO PERSONAL");
        lblRegistro.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #cda434; -fx-font-family: 'Consolas';");

        TextField txtNuevoId = new TextField(); txtNuevoId.setPromptText("Asignar ID (ej. A-002)"); txtNuevoId.setStyle(estiloCampos);
        TextField txtNombre = new TextField(); txtNombre.setPromptText("Nombre Completo"); txtNombre.setStyle(estiloCampos);
        TextField txtEmail = new TextField(); txtEmail.setPromptText("Correo (ej. nombre@dominio.com)"); txtEmail.setStyle(estiloCampos);

        ComboBox<String> cbNivel = new ComboBox<>();
        cbNivel.getItems().addAll("Tier 1", "Tier 2", "Admin SOC");
        cbNivel.setPromptText("Nivel de Privilegio");
        cbNivel.setStyle(estiloCampos);

        TextField txtClaveToken = new TextField(); txtClaveToken.setPromptText("Asignar Clave Token"); txtClaveToken.setStyle(estiloCampos);

        Button btnRegistrar = new Button("REGISTRAR ANALISTA");
        btnRegistrar.setPrefWidth(Double.MAX_VALUE);
        btnRegistrar.setStyle("-fx-background-color: #2a2a35; -fx-text-fill: #cda434; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: #cda434;");

        Label lblMensajeReg = new Label();
        lblMensajeReg.setStyle("-fx-font-family: 'Consolas'; -fx-font-weight: bold;");

        btnRegistrar.setOnAction(e -> {
            if (txtNuevoId.getText().isEmpty() || txtClaveToken.getText().isEmpty() || txtEmail.getText().isEmpty() || txtNombre.getText().isEmpty()) {
                lblMensajeReg.setText("> ERROR: Todos los campos son requeridos.");
                lblMensajeReg.setStyle("-fx-text-fill: #ff3333;");
                return;
            }

            // ---------------------------------------------------------
            // VALIDACIÓN ESTRICTA DE CORREO POR EXPRESIÓN REGULAR (Regex)
            // ---------------------------------------------------------
            String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)\\.([A-Za-z]{2,})$";
            if (!Pattern.matches(emailRegex, txtEmail.getText())) {
                lblMensajeReg.setText("> ERROR: Formato de correo inválido.");
                lblMensajeReg.setStyle("-fx-text-fill: #ff3333;");
                return;
            }

            String idTokenAsignado = "TK-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            TokenMFA nuevoToken = new TokenMFA(idTokenAsignado, txtClaveToken.getText(), EstadoToken.ACTIVO);

            AnalistaSeguridad nuevoAnalista = new AnalistaSeguridad(
                    txtNuevoId.getText(), txtNombre.getText(), txtEmail.getText(),
                    cbNivel.getValue() != null ? cbNivel.getValue() : "Tier 1", nuevoToken
            );

            moduloMFA.crearAnalista(nuevoAnalista);
            lblMensajeReg.setText("> ÉXITO: Analista " + nuevoAnalista.getNombre() + " registrado.");
            lblMensajeReg.setStyle("-fx-text-fill: #39ff14;");

            txtNuevoId.clear(); txtNombre.clear(); txtEmail.clear(); txtClaveToken.clear(); cbNivel.getSelectionModel().clearSelection();
        });

        panelRegistro.getChildren().addAll(lblRegistro, txtNuevoId, txtNombre, txtEmail, cbNivel, txtClaveToken, btnRegistrar, lblMensajeReg);

        this.getChildren().addAll(panelLogin, panelRegistro);
    }
}