package main.java.com.emergencias.alert;

import main.java.com.emergencias.model.EmergencyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase para empaquetar y "enviar" la alerta.
 * Persistencia simple en archivo de log.
 */
public class AlertSender {
    private final String destinoAlerta;
    private static final String LOG_FILE = "src/main/java/resources/emergency_log.txt";

    public AlertSender(String destinoAlerta) {
        this.destinoAlerta = destinoAlerta;
    }

    /**
     * Empaqueta el evento y lo simula como enviado.
     * @param event El evento de emergencia confirmado.
     */
    public void sendAlert(EmergencyEvent event) {
        if (event == null) {
            System.err.println("No se puede enviar la alerta: Evento nulo.");
            return;
        }

        String paqueteDeDatos = event.toString();

        // Simulación de contacto y persistencia
        System.out.println("\n*** ENVIANDO ALERTA ***");
        System.out.println(String.format(">> SIMULACIÓN DE ENVÍO A DESTINO: %s <<", destinoAlerta));
        System.out.println("Paquete de Datos Empaquetado:\n" + paqueteDeDatos);

        persistAlert(paqueteDeDatos);
        notifyContacts(event.getDatosUsuario().getTelefonoContacto());
    }

    /**
     * Simula la persistencia del paquete de alerta en un archivo.
     * @param data El string de datos del evento.
     */
    private void persistAlert(String data) {
        java.io.File file = new java.io.File(LOG_FILE);
        java.io.File parentDir = file.getParentFile();

        // Control de errores, asegurar que el directirio existe
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdir();
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {
            out.println("REGISTRO DE ALERTA - " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            out.println(data);
            System.out.println("\n[PERSISTENCIA] Alerta registrada con éxito.");
        } catch (IOException e) {
            System.err.println("[ERROR] No se pudo escribir en el archivo de log: " + e.getMessage());
        }
    }

    /**
     * Simula la notificación a contactos personales.
     * @param telefono El número de contacto personal.
     */
    public void notifyContacts(String telefono) {
        System.out.println(String.format("[CONTACTOS] Se ha enviado una notificación SMS/Llamada simulada al contacto: %s", telefono));
    }
}
