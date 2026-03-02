package main.java.com.emergencias.controller;

import main.java.com.emergencias.alert.AlertSender;
import main.java.com.emergencias.detector.EmergencyDetector;
import main.java.com.emergencias.model.EmergencyEvent;
import main.java.com.emergencias.model.UserData;

import java.io.FileWriter; // Para guardar archivos
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner; // Para leer del teclado

public class EmergencyManager {
    private final EmergencyDetector detector;
    private final AlertSender sender;
    private final UserData usuarioSimulado;

    public EmergencyManager(int umbralActivacion, String destinoAlerta) {
        this.detector = new EmergencyDetector(umbralActivacion);
        this.sender = new AlertSender(destinoAlerta);

        // MEJORA: Interacción con el usuario en lugar de datos fijos
        Scanner sc = new Scanner(System.in);
        System.out.println("--- CONFIGURACIÓN DEL DISPOSITIVO ---");
        System.out.print("Introduzca nombre del titular: ");
        String nombre = sc.nextLine();
        System.out.print("Introduzca observaciones médicas (ej. Alergias): ");
        String obs = sc.nextLine();

        this.usuarioSimulado = new UserData(
                nombre,
                "600-112-112",
                obs
        );

        System.out.println("\n[OK] Sistema Inicializado para: " + usuarioSimulado.getNombre());
    }

    public void startSystem() {
        try {
            EmergencyEvent evento = detector.detectEvent(usuarioSimulado);

            if (evento != null) {
                sender.sendAlert(evento);
                // MEJORA: Almacenamiento en fichero local
                guardarEnHistorial(evento);
                System.out.println("\nProceso completado. Alerta registrada en historial_emergencias.txt");
            } else {
                System.out.println("\nProceso terminado. No se detectó impacto suficiente.");
            }
        } catch (Exception e) {
            System.err.println("\n Fallo durante la orquestación: " + e.getMessage());
        }
    }

    // MÉTODO NUEVO para la mejora de almacenamiento
    private void guardarEnHistorial(EmergencyEvent evento) {
        try (FileWriter fw = new FileWriter("historial_emergencias.txt", true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println("--- REGISTRO DE EMERGENCIA ---");
            pw.println(evento.toString());
            pw.println("------------------------------");
        } catch (IOException e) {
            System.err.println("No se pudo guardar el historial: " + e.getMessage());
        }
    }
}