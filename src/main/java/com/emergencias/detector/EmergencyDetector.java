package main.java.com.emergencias.detector;

import main.java.com.emergencias.model.EmergencyEvent;
import main.java.com.emergencias.model.Hospital;
import main.java.com.emergencias.model.HospitalLoader;
import main.java.com.emergencias.model.UserData;

public class EmergencyDetector {
    private final int UMBRAL_ACTIVACION;

    public EmergencyDetector(int umbral) {
        this.UMBRAL_ACTIVACION = umbral;
    }

    // Este es el método que llamará el Manager
    public EmergencyEvent processFromGUI(UserData userData, String tipo, String modoUbicacion,
                                         String direccionManual, String latStr, String lonStr, int gravedad) {

        System.out.println("--- Procesando Emergencia desde GUI ---");
        String ubicacionFinal = "";

        // Lógica de Ubicación (Copiada de tu gatherEventData)
        if ("2".equals(modoUbicacion)) {
            System.out.println("DEBUG lat: '" + latStr + "' lon: '" + lonStr + "'");
            try {
                double lat = Double.parseDouble(latStr);
                double lon = Double.parseDouble(lonStr);
                System.out.println("DEBUG parsed lat: " + lat + " lon: " + lon);

                HospitalLoader loader = new HospitalLoader();
                loader.cargarDatos();
                Hospital cercano = loader.encontrarMasCercano(lat, lon);

                if (cercano != null) {
                    double dist = cercano.calcularDistanciaA(lat, lon);
                    ubicacionFinal = String.format("Cerca de: %s (%s) a %.2f km",
                            cercano.getNombre(), cercano.getMunicipio(), dist);
                } else {
                    ubicacionFinal = "Coordenadas: " + lat + ", " + lon;
                }
            } catch (Exception e) {
                ubicacionFinal = "Error GPS: " + direccionManual;
            }
        } else {
            ubicacionFinal = direccionManual;
        }

        // Validación de gravedad
        // Al final de processFromGUI(), cambia el return:
        if (gravedad >= UMBRAL_ACTIVACION) {
            return new EmergencyEvent(tipo, ubicacionFinal, userData, modoUbicacion); // ← pasa el modo
        } else {
            return null;
        }
    }
}