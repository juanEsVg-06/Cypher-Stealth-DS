package com.cypherstealth.GUI;

import com.cypherstealth.ModuloGestionMFA;
import com.cypherstealth.model.AnalistaSeguridad;
import com.cypherstealth.model.TokenMFA;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;

public class VistaMFA extends VBox {

    private ModuloGestionMFA moduloMFA;

    public VistaMFA(ModuloGestionMFA moduloMFA) {
        this.moduloMFA = moduloMFA;
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        this.setPadding(new Insets(30));
        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);

        // Estilo global de la ventana (Fondo oscuro)
        this.setStyle("-fx-background-color: #121216;");

        // --- CARGA DEL LOGO ---
        ImageView imgLogo = new ImageView();
        try {
            // Busca el archivo logo.png en la raíz del proyecto
            File file = new File("logo.png");
            Image image = new Image(file.toURI().toString());
            imgLogo.setImage(image);
            imgLogo.setFitWidth(120);
            imgLogo.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Logo no encontrado en la ruta especificada.");
        }

        Label lblTitulo = new Label("BÓVEDA MFA");
        // Color cobre/dorado para el título
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #cda434; -fx-font-family: 'Consolas';");

        // Componentes de Login con estilo oscuro
        String estiloCampos = "-fx-background-color: #1e1e24; -fx-text-fill: #39ff14; -fx-border-color: #5e2c8f; -fx-border-width: 1px; -fx-prompt-text-fill: #555555;";

        TextField txtIdAnalista = new TextField();
        txtIdAnalista.setPromptText("ID del Analista");
        txtIdAnalista.setMaxWidth(220);
        txtIdAnalista.setStyle(estiloCampos);

        PasswordField txtToken = new PasswordField();
        txtToken.setPromptText("Token Físico (MFA)");
        txtToken.setMaxWidth(220);
        txtToken.setStyle(estiloCampos);

        // Botones con el morado del logo
        String estiloBoton = "-fx-background-color: #5e2c8f; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: #cda434; -fx-border-radius: 3px;";

        Button btnLogin = new Button("AUTENTICAR");
        btnLogin.setPrefWidth(220);
        btnLogin.setStyle(estiloBoton);

        Label lblMensaje = new Label();
        lblMensaje.setStyle("-fx-font-family: 'Consolas'; -fx-font-weight: bold;");

        // Evento del botón
        btnLogin.setOnAction(e -> {
            String id = txtIdAnalista.getText();
            String token = txtToken.getText();

            AnalistaSeguridad analista = moduloMFA.buscarAnalista(id);
            if (analista != null) {
                if (moduloMFA.validarToken(analista, token)) {
                    lblMensaje.setText("> ACCESO CONCEDIDO: " + analista.getNombre());
                    lblMensaje.setStyle("-fx-text-fill: #39ff14;"); // Verde neón
                } else {
                    lblMensaje.setText("> ERROR: TOKEN MFA INVÁLIDO");
                    lblMensaje.setStyle("-fx-text-fill: #ff3333;"); // Rojo alerta
                }
            } else {
                lblMensaje.setText("> ERROR: ANALISTA DESCONOCIDO");
                lblMensaje.setStyle("-fx-text-fill: #ff3333;");
            }
        });

        Button btnCrearDummy = new Button("Inyectar Analista Prueba");
        btnCrearDummy.setStyle("-fx-background-color: transparent; -fx-text-fill: #888888; -fx-underline: true; -fx-cursor: hand;");
        btnCrearDummy.setOnAction(e -> {
            TokenMFA tokenDummy = new TokenMFA("TK-001", "123456", EstadoToken.ACTIVO);
            AnalistaSeguridad dummy = new AnalistaSeguridad("A-001", "Admin SOC", "admin@udla.edu.ec", "Tier 3", tokenDummy);
            moduloMFA.crearAnalista(dummy);
            lblMensaje.setText("> SYS: Analista A-001 (Token: 123456) inyectado.");
            lblMensaje.setStyle("-fx-text-fill: #cda434;"); // Cobre
        });

        this.getChildren().addAll(imgLogo, lblTitulo, txtIdAnalista, txtToken, btnLogin, lblMensaje, btnCrearDummy);
    }
}