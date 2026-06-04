package com.cypherstealth;

import com.cypherstealth.GUI.VistaMFA;
import com.cypherstealth.GUI.VistaForense;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Inicializamos el 'Backend' (El Facade central del sistema)
        SistemaCypherStealthIDS sistema = new SistemaCypherStealthIDS();
        sistema.iniciar();

        // 2. Inicializamos el 'Frontend' (Las vistas) pasándole sus respectivos módulos lógicos
        VistaMFA vistaMFA = new VistaMFA(sistema.getMfa());
        VistaForense vistaForense = new VistaForense(sistema.getForense());

        // 3. Organizamos las vistas en un panel con pestañas (Tabs)
        TabPane tabPane = new TabPane();

        Tab tabMFA = new Tab("Bóveda MFA", vistaMFA);
        tabMFA.setClosable(false); // Evita que el usuario cierre la pestaña por accidente

        Tab tabForense = new Tab("Trazabilidad Forense", vistaForense);
        tabForense.setClosable(false);

        tabPane.getTabs().addAll(tabMFA, tabForense);

        // 4. Configuramos la ventana principal (Stage)
        Scene scene = new Scene(tabPane, 800, 600); // Tamaño inicial de la ventana

        // (Opcional) Aquí más adelante podrías cargar un archivo CSS para darle un tema oscuro/hacker
        // scene.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());

        primaryStage.setTitle("Cypher Stealth-DS | SOC Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Este es el disparador que levanta la interfaz gráfica
        launch(args);
    }
}