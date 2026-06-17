package com.cypherstealth;

import com.cypherstealth.GUI.VistaMFA;
import com.cypherstealth.GUI.VistaForense;
import com.cypherstealth.GUI.VistaTriage;
import com.cypherstealth.GUI.VistaInventario;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        SistemaCypherStealthIDS sistema = new SistemaCypherStealthIDS();
        sistema.iniciar();

        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: #121216; -fx-tab-min-width: 150px;");

        VistaTriage vistaTriage = new VistaTriage(sistema.getTriage());
        VistaForense vistaForense = new VistaForense(sistema.getForense());
        VistaInventario vistaInventario = new VistaInventario(sistema.getInventario());

        // Instanciamos los Tabs BLOQUEADOS por defecto
        Tab tabTriage = new Tab("Triage de Alertas", vistaTriage);
        tabTriage.setClosable(false);
        tabTriage.setDisable(true); // ¡Bloqueado!

        Tab tabForense = new Tab("Trazabilidad Forense", vistaForense);
        tabForense.setClosable(false);
        tabForense.setDisable(true); // ¡Bloqueado!

        Tab tabInventario = new Tab("Inventario & Topología", vistaInventario);
        tabInventario.setClosable(false);
        tabInventario.setDisable(true); // ¡Bloqueado!

        // Instanciamos la vista MFA pasándole el control absoluto de los Tabs
        VistaMFA vistaMFA = new VistaMFA(sistema.getMfa(), tabTriage, tabForense, tabInventario, tabPane);

        Tab tabMFA = new Tab("Bóveda MFA", vistaMFA);
        tabMFA.setClosable(false);

        tabPane.getTabs().addAll(tabMFA, tabTriage, tabForense, tabInventario);

        Scene scene = new Scene(tabPane, 1000, 700);
        primaryStage.setTitle("Cypher Stealth-DS | SOC Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}