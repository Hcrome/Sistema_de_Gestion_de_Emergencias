package main.java.com.emergencias.controller;

import main.java.com.emergencias.alert.AlertSender;
import main.java.com.emergencias.detector.EmergencyDetector;
import main.java.com.emergencias.model.EmergencyEvent;
import main.java.com.emergencias.model.UserData;
import java.io.FileWriter;
import java.io.PrintWriter;

public class EmergencyManager {
    private final EmergencyDetector detector;
    private final AlertSender sender;
    private final UserData usuarioSimulado;

    public EmergencyManager(int umbralActivacion, String destinoAlerta, String nombre, String obs) {
        this.detector = new EmergencyDetector(umbralActivacion);
        this.sender = new AlertSender(destinoAlerta);
        this.usuarioSimulado = new UserData(nombre, "600-112-112", obs);
    }

    public EmergencyEvent startSystem(String tipo, String modoUbic, String dir,
                                      String lat, String lon, int grav) {
        EmergencyEvent evento = detector.processFromGUI(usuarioSimulado, tipo, modoUbic, dir, lat, lon, grav);

        if (evento != null) {
            sender.sendAlert(evento);
            guardarEnHistorial(evento);
        }
        return evento; //  devuelve el evento (o null si fue falso positivo)
    }

   // registro añadido en practica de git hub por un compañero (duplicado)
    private void guardarEnHistorial(EmergencyEvent evento) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("historial_emergencias.txt", true))) {
            pw.println("--- REGISTRO DE EMERGENCIA ---");
            pw.println(evento.toString());
            pw.println("------------------------------");
        } catch (Exception e) {
            System.err.println("No se pudo guardar: " + e.getMessage());
        }
    }
}
