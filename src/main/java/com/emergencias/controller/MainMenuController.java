package main.java.com.emergencias.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainMenuController {
    // Método para el botón de Emergencias
    @FXML
    void abrirEmergencia(ActionEvent event) {
        cambiarEscena(event, "/view/formulario_emergencia.fxml", "Registro de Emergencia");
    }
    // Método para el botón de Instrucciones
    @FXML
    void abrirInstrucciones(ActionEvent event) {
        cambiarEscena(event, "/view/instrucciones.fxml", "Instrucciones de Uso");
    }
    // MÉTODO GENÉRICO PARA CAMBIAR DE VENTANA
    private void cambiarEscena(ActionEvent event, String fxmlPath, String titulo) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error: No se pudo cargar el archivo FXML en " + fxmlPath);
            e.printStackTrace();
        }
    }
}
