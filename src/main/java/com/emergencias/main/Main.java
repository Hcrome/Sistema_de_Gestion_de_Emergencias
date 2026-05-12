package main.java.com.emergencias.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.java.com.emergencias.controller.UserDataController;
import main.java.com.emergencias.controller.EmergencyListController;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Carga del Menú Principal (Main_menu.fxml)
            File fileMenu = new File("src/main/resources/Main_menu.fxml");
            URL urlMenu = fileMenu.toURI().toURL();
            FXMLLoader loaderMenu = new FXMLLoader(urlMenu);
            Parent root = loaderMenu.load();

            // --- BOTÓN 1: INICIAR REPORTE (Abre el formulario) ---
            Button btnEmergencia = (Button) root.lookup("#inicEmergencia");
            if (btnEmergencia != null) {
                btnEmergencia.setOnAction(e -> {
                    try {
                        File fileForm = new File("src/main/resources/user_data_form.fxml");
                        URL urlForm = fileForm.toURI().toURL();
                        FXMLLoader loaderForm = new FXMLLoader(urlForm);
                        Parent formRoot = loaderForm.load();

                        // Iniciamos controlador pasándole el stage para futuros cambios
                        new UserDataController(formRoot, primaryStage);
                        primaryStage.getScene().setRoot(formRoot);

                    } catch (Exception ex) {
                        System.err.println("Error al cargar formulario: " + ex.getMessage());
                    }
                });
            }

            // --- BOTÓN 2: CONSULTAR INSTRUCCIONES (Abre la lista del JSON) ---
            Button btnInstrucciones = (Button) root.lookup("#InicInstrucciones");
            if (btnInstrucciones != null) {
                btnInstrucciones.setOnAction(e -> {
                    try {
                        File fileList = new File("src/main/resources/emergency_list.fxml");
                        URL urlList = fileList.toURI().toURL();
                        FXMLLoader loaderList = new FXMLLoader(urlList);
                        Parent listRoot = loaderList.load();

                        // Iniciamos controlador de la lista (Jackson leerá el JSON aquí)
                        new EmergencyListController(listRoot, primaryStage);
                        primaryStage.getScene().setRoot(listRoot);

                    } catch (Exception ex) {
                        System.err.println("Error al cargar instrucciones: " + ex.getMessage());
                    }
                });
            }

            // 2. Configuración de la Ventana Principal
            Scene scene = new Scene(root);
            primaryStage.setTitle("SISTEMA DE EMERGENCIAS V3 - JavaFX");
            primaryStage.setScene(scene);
            primaryStage.show();

            System.out.println("DEBUG: Aplicación iniciada y botones vinculados.");

        } catch (Exception e) {
            System.err.println("ERROR CRÍTICO EN START:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}