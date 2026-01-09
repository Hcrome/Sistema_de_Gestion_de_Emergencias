package main.java.com.emergencias.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Modelo para el evento de emergencia. Contiene los datos recopilados.
 */
public class EmergencyEvent {
    private final String id;
    private final String tipoEmergencia;
    private final String ubicacion; // Simulación de LatLong
    private final LocalDateTime timestamp;
    private final UserData datosUsuario;
    private final String prioridad;

    public EmergencyEvent(String tipoEmergencia, String ubicacion, UserData datosUsuario) {
        // Generar un ID simple basado en el tiempo
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.id = "E" + LocalDateTime.now().format(formatter);
        this.tipoEmergencia = tipoEmergencia;
        this.ubicacion = ubicacion;
        this.timestamp = LocalDateTime.now();
        this.datosUsuario = datosUsuario;
        this.prioridad = asignarPrioridad(tipoEmergencia);
    }

    // feature de emergency priority
    private String asignarPrioridad(String tipo) {
        String t = tipo.toLowerCase();
        if (t.contains("fuego") || t.contains("accidente") || t.contains("cardiaco")) return "ALTA";
        if (t.contains("sanitaria") || t.contains("robo")) return "MEDIA";
        return "BAJA";

    }

    // Getters
    public String getId() { return id; }
    public String getTipoEmergencia() { return tipoEmergencia; }
    public String getUbicacion() { return ubicacion; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public UserData getDatosUsuario() { return datosUsuario; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format(
                "| ID: %s | Tipo: %s | Hora: %s | Ubicación: %s | %s | Prioridad: %s ",
                id, tipoEmergencia, timestamp.format(formatter), ubicacion, datosUsuario, prioridad.toString() // añadimos la variable al mensaje
        );
    }
}