package main.java.com.emergencias.controller;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.File;
import java.net.URL;

public class UserDataController {

    private Parent root;
    private Stage stage; // El Stage es vital para cambiar la escena

    // CONSTRUCTOR MODIFICADO: Ahora acepta el Stage del Main
    public UserDataController(Parent root, Stage stage) {
        this.root = root;
        this.stage = stage;
        configurarInterfaz();
    }

    private void configurarInterfaz() {
        // 1. Localización de los campos (Asegúrate de que los IDs coincidan en el FXML)
        TextField txtNombre = (TextField) root.lookup("#txtNombre");
        TextField txtAlergias = (TextField) root.lookup("#txtAlergias");
        TextField txtTipo = (TextField) root.lookup("#txtTipo");
        Slider sliderGravedad = (Slider) root.lookup("#sliderGravedad");
        TextField txtFisico = (TextField) root.lookup("#txtFisico");
        TextField txtLatitud = (TextField) root.lookup("#txtLatitud");
        TextField txtLongitud = (TextField) root.lookup("#txtLongitud");

        Button btnEnviar = (Button) root.lookup("#btnEnviar");

        if (btnEnviar != null) {
            btnEnviar.setOnAction(e -> {
                // 2. Captura de datos
                String nombre = (txtNombre != null) ? txtNombre.getText() : "Usuario";
                String obs = (txtAlergias != null) ? txtAlergias.getText() : "Ninguna";
                String tipo = (txtTipo != null) ? txtTipo.getText() : "Urgencia";
                int gravedad = (sliderGravedad != null) ? (int) sliderGravedad.getValue() : 5;

                boolean esFisico = (txtFisico != null && txtFisico.isVisible());
                String modoUbic = esFisico ? "1" : "2";
                String dir = (txtFisico != null) ? txtFisico.getText() : "";
                String lat = (txtLatitud != null) ? txtLatitud.getText() : "0.0";
                String lon = (txtLongitud != null) ? txtLongitud.getText() : "0.0";

                try {
                    // 3. CARGAR PANTALLA DE LOG (emergency_log.fxml)
                    File fileLog = new File("src/main/resources/emergency_log.fxml");
                    URL urlLog = fileLog.toURI().toURL();
                    FXMLLoader loaderLog = new FXMLLoader(urlLog);
                    Parent logRoot = loaderLog.load();

                    // 4. BUSCAR EL TEXTAREA EN EL NUEVO FXML
                    TextArea txtLog = (TextArea) logRoot.lookup("#txtLog");

                    // 5. EJECUTAR LÓGICA DEL MANAGER (Tu clase Core)
                    EmergencyManager manager = new EmergencyManager(3, "112", nombre, obs);
                    manager.startSystem(tipo, modoUbic, dir, lat, lon, gravedad);

                    // 6. VOLCAR RESULTADOS AL TEXTAREA
                    if (txtLog != null) {
                        txtLog.appendText("--- REPORTE DE EMERGENCIA ENVIADO ---\n");
                        txtLog.appendText("Usuario: " + nombre + "\n");
                        txtLog.appendText("Tipo: " + tipo + " | Impacto: " + gravedad + "\n");
                        txtLog.appendText("Ubicación: " + (esFisico ? dir : "Coordenadas GPS") + "\n");
                        txtLog.appendText("-------------------------------------\n");
                        txtLog.appendText("Estado: Notificación al 112 completada.\n");
                    }

                    // 7. ¡ZAS! CAMBIO DE ESCENA EN EL STAGE
                    stage.getScene().setRoot(logRoot);

                } catch (Exception ex) {
                    System.err.println("Error al saltar al log: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        }
    }
}