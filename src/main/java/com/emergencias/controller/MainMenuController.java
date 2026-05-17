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

    @FXML
    private void abrirEmergencia(ActionEvent event) {
        cambiarEscena(event, "/user_data_form.fxml", "Registro de Emergencia");
    }

    @FXML
    private void abrirInstrucciones(ActionEvent event) {
        cambiarEscena(event, "/emergency_list.fxml", "Instrucciones");
    }

    private void cambiarEscena(ActionEvent event, String fxmlPath, String titulo) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
