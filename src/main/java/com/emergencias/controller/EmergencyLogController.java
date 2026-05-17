package main.java.com.emergencias.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import main.java.com.emergencias.model.EmergencyEvent;

public class EmergencyLogController {

    @FXML private TextArea txtLog;

    public void mostrarEvento(EmergencyEvent evento) {
        if (evento != null) {
            boolean esGPS = "2".equals(evento.getModoUbicacion());

            String lineaUbicacion = esGPS
                    ? "🏥 Hospital más cercano\n   " + evento.getUbicacion()
                    : ""; // modo manual: no muestra nada

            txtLog.setText(
                    "--- SISTEMA DE EMERGENCIAS ---\n\n" +
                            "ID:        " + evento.getId() + "\n" +
                            "Tipo:      " + evento.getTipoEmergencia() + "\n" +
                            "Prioridad: " + evento.getPrioridad() + "\n" +
                            "Hora:      " + evento.getTimestamp() + "\n\n" +
                            "👤 Usuario\n" +
                            "   Nombre:      " + evento.getDatosUsuario().getNombre() + "\n" +
                            "   Info médica: " + evento.getDatosUsuario().getInfoMedica() + "\n\n" +
                            lineaUbicacion + "\n" +
                            "------------------------------\n" +
                            "Estado: Notificación al 112 completada."
            );
        } else {
            txtLog.setText(
                    "⚠️ Emergencia no activada.\n" +
                            "La gravedad introducida es inferior al umbral mínimo (3)."
            );
        }
    }
}
