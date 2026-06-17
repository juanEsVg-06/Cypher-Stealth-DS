package com.cypherstealth.GUI;

import com.cypherstealth.ModuloInventarioActivos;
import com.cypherstealth.model.ActivoRed;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VistaInventario extends HBox {

    private ModuloInventarioActivos moduloInventario;
    private Pane canvasRed; // El lienzo "Packet Tracer"
    private ComboBox<String> cbOrigen;
    private ComboBox<String> cbDestino;

    // Guardamos las coordenadas visuales de cada equipo para dibujar las líneas
    private Map<String, double[]> coordenadasNodos;

    public VistaInventario(ModuloInventarioActivos moduloInventario) {
        this.moduloInventario = moduloInventario;
        this.coordenadasNodos = new HashMap<>();
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        this.setStyle("-fx-background-color: #121216;"); // Fondo SOC
        this.setPadding(new Insets(20));
        this.setSpacing(20);

        // --- PANEL IZQUIERDO: CONTROLES ---
        VBox panelControles = new VBox(15);
        panelControles.setPrefWidth(250);

        Label lblTitulo = new Label("TOPOLOGÍA DE RED");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #cda434; -fx-font-family: 'Consolas';");

        // Formulario de creación
        TextField txtId = new TextField(); txtId.setPromptText("ID (ej. SRV-01)");
        TextField txtIp = new TextField(); txtIp.setPromptText("IP (ej. 192.168.1.10)");
        TextField txtCrit = new TextField(); txtCrit.setPromptText("Criticidad (1-10)");
        Button btnAgregar = new Button("Agregar Activo");
        btnAgregar.setStyle("-fx-background-color: #5e2c8f; -fx-text-fill: white; -fx-font-weight: bold;");

        // Formulario de conexión
        Label lblConexion = new Label("Conectar Equipos (Grafo):");
        lblConexion.setStyle("-fx-text-fill: #39ff14; -fx-font-family: 'Consolas';");
        cbOrigen = new ComboBox<>(); cbOrigen.setPromptText("Origen");
        cbDestino = new ComboBox<>(); cbDestino.setPromptText("Destino");
        Button btnConectar = new Button("Crear Enlace");
        btnConectar.setStyle("-fx-background-color: #2a2a35; -fx-text-fill: #cda434;");

        // Botón Simulación
        Button btnSimular = new Button("Simular Ruta de Ataque");
        btnSimular.setStyle("-fx-background-color: #8b0000; -fx-text-fill: white; -fx-font-weight: bold;");

        panelControles.getChildren().addAll(lblTitulo, new Label("Nuevo Equipo:"), txtId, txtIp, txtCrit, btnAgregar,
                new Separator(), lblConexion, cbOrigen, cbDestino, btnConectar,
                new Separator(), btnSimular);

        // --- PANEL DERECHO: CANVAS DE RED (Packet Tracer Style) ---
        canvasRed = new Pane();
        canvasRed.setPrefSize(500, 500);
        canvasRed.setStyle("-fx-background-color: #0a0a0c; -fx-border-color: #5e2c8f; -fx-border-width: 2px;");
        HBox.setHgrow(canvasRed, Priority.ALWAYS); // Que ocupe el resto de la pantalla

        // --- LÓGICA DE EVENTOS ---
        btnAgregar.setOnAction(e -> {
            try {
                ActivoRed nuevo = new ActivoRed(txtId.getText(), txtIp.getText(), "00:00:00:00", Integer.parseInt(txtCrit.getText()));
                moduloInventario.registrarActivo(nuevo);
                actualizarInterfaz();
                txtId.clear(); txtIp.clear(); txtCrit.clear();
            } catch (Exception ex) {
                // Manejo básico de error si no ingresa un número en criticidad
            }
        });

        btnConectar.setOnAction(e -> {
            String idOrigen = cbOrigen.getValue();
            String idDestino = cbDestino.getValue();
            if (idOrigen != null && idDestino != null && !idOrigen.equals(idDestino)) {
                ActivoRed origen = buscarActivoPorId(idOrigen);
                ActivoRed destino = buscarActivoPorId(idDestino);
                moduloInventario.conectar(origen, destino);
                actualizarInterfaz();
            }
        });

        btnSimular.setOnAction(e -> {
            // Lógica visual para la simulación: se podría pintar de rojo el camino
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "La simulación de ataque ejecutará Dijkstra/Ruta sobre el grafo.");
            alert.show();
        });

        this.getChildren().addAll(panelControles, canvasRed);
    }

    private ActivoRed buscarActivoPorId(String id) {
        for (ActivoRed a : moduloInventario.obtenerTodosLosActivos()) {
            if (a.getIdActivo().equals(id)) return a;
        }
        return null;
    }

    // Método que dibuja la topología basada en el GrafoRed
    private void actualizarInterfaz() {
        canvasRed.getChildren().clear();
        cbOrigen.getItems().clear();
        cbDestino.getItems().clear();

        Random rand = new Random();

        // 1. Asignar coordenadas a los nodos y dibujar conexiones (Aristas/Edges)
        for (ActivoRed origen : moduloInventario.obtenerTodosLosActivos()) {
            String idOrigen = origen.getIdActivo();
            cbOrigen.getItems().add(idOrigen);
            cbDestino.getItems().add(idOrigen);

            // Generar posición visual si es nuevo
            if (!coordenadasNodos.containsKey(idOrigen)) {
                coordenadasNodos.put(idOrigen, new double[]{rand.nextInt(400) + 50, rand.nextInt(400) + 50});
            }

            // Dibujar cables (Líneas) a sus vecinos
            double[] posOrigen = coordenadasNodos.get(idOrigen);
            List<ActivoRed> vecinos = moduloInventario.obtenerVecinos(origen);
            for (ActivoRed vecino : vecinos) {
                if (coordenadasNodos.containsKey(vecino.getIdActivo())) {
                    double[] posDestino = coordenadasNodos.get(vecino.getIdActivo());
                    Line cable = new Line(posOrigen[0], posOrigen[1], posDestino[0], posDestino[1]);
                    cable.setStroke(Color.web("#555555"));
                    cable.setStrokeWidth(2);
                    canvasRed.getChildren().add(cable);
                }
            }
        }

        // 2. Dibujar Nodos (Vértices) encima de las líneas
        for (ActivoRed activo : moduloInventario.obtenerTodosLosActivos()) {
            double[] pos = coordenadasNodos.get(activo.getIdActivo());

            // Círculo del equipo
            Circle nodo = new Circle(pos[0], pos[1], 15);
            nodo.setFill(activo.esCritico() ? Color.web("#8b0000") : Color.web("#39ff14"));
            nodo.setStroke(Color.web("#cda434"));

            // Etiqueta de texto (ID y Severidad)
            Text etiqueta = new Text(pos[0] - 15, pos[1] - 20, activo.getIdActivo());
            etiqueta.setFill(Color.WHITE);
            etiqueta.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 10px;");

            canvasRed.getChildren().addAll(nodo, etiqueta);
        }
    }
}