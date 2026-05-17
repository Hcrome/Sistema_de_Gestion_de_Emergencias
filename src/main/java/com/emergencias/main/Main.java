package main.java.com.emergencias.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Main_menu.fxml"));
        primaryStage.setTitle("Sistema de Emergencias");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}