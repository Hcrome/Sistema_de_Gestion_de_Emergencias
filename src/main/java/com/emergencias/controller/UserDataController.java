package main.java.com.emergencias.controller;

import javafx.event.ActionEvent;        // ✅ JavaFX, no AWT
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.java.com.emergencias.model.EmergencyEvent;

public class UserDataController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtAlergias;
    @FXML private TextField txtTipo;
    @FXML private Slider    sliderGravedad;
    @FXML private TextField txtFisico;
    @FXML private TextField txtLatitud;
    @FXML private TextField txtLongitud;
    @FXML private Button    btnEnviar;
    @FXML private HBox      panelCoordenadas;
    @FXML private SplitMenuButton splitUbicacion;

    // logica del split menu de direcciones.
    @FXML
    public void initialize() {
        panelCoordenadas.setVisible(false);

        splitUbicacion.getItems().get(0).setOnAction(e -> {
            splitUbicacion.setText("Direccion fisica");
            txtFisico.setVisible(true);
            panelCoordenadas.setVisible(false);
        });

        splitUbicacion.getItems().get(1).setOnAction(e -> {
            splitUbicacion.setText("Coordenadas");
            txtFisico.setVisible(false);
            panelCoordenadas.setVisible(true);
        });
    }

    @FXML
    private void enviarEmergencia(ActionEvent event) {
        String nombre    = txtNombre.getText();
        String obs       = txtAlergias.getText();
        String tipo      = txtTipo.getText();
        int gravedad     = (int) sliderGravedad.getValue();
        boolean esFisico = !txtFisico.getText().isEmpty();
        String modoUbic  = esFisico ? "1" : "2";
        String dir       = txtFisico.getText();
        String lat       = txtLatitud.getText();
        String lon       = txtLongitud.getText();


        try {
            // Ejecutar el core y capturar el evento
            EmergencyManager manager = new EmergencyManager(3, "112", nombre, obs);
            EmergencyEvent evento = manager.startSystem(tipo, modoUbic, dir, lat, lon, gravedad);

            // Cargar el log y obtener su controlador
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/emergency_log.fxml"));
            Parent logRoot = loader.load();
            EmergencyLogController logController = loader.getController();

            // Pasar el evento al controlador del log
            logController.mostrarEvento(evento);

            // Cambiar escena
            Stage stage = (Stage) btnEnviar.getScene().getWindow();
            stage.getScene().setRoot(logRoot);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
