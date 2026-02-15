package main.java.com.emergencias.main;
import main.java.com.emergencias.model.HospitalLoader;
import main.java.com.emergencias.model.Hospital;
import main.java.com.emergencias.controller.EmergencyManager;

 // Punto de entrada principal para la simulación del Módulo Core de Emergencias.

public class Main {

    // Parámetros de configuración del sistema

    private static final int UMBRAL_REQUERIDO = 5; // Simulación: Valor mínimo de impacto para activar
    private static final String DESTINO_SERVICIO = "112 Servicios de Emergencia";

    public static void main(String[] args) {
        System.out.println("  MÓDULO CORE PARA SISTEMAS DE EMERGENCIA (JAVA)  ");

        // Instancia el controlador y configura los parámetros del sistema
        EmergencyManager manager = new EmergencyManager(UMBRAL_REQUERIDO, DESTINO_SERVICIO);

        // Inicia el flujo de detección y alerta
        manager.startSystem();

        System.out.println("Simulación Finalizada.");

        // METODO DE LA CLASE HOSPITALLOADER QUE VERIFICA QUE FUNCIONA (Modificar al gusto del nuevo metodo)
        HospitalLoader loader = new HospitalLoader();
        loader.cargarDatos();
        /** esto es una prueba de que los datos son recogidos correctamente, pasa los datos JSON a texto plano e imprime por consola

        try {
            // 1. Leer el archivo como texto plano (puro String)
            String ruta = "src/main/java/resources/hospital_list.json";
            String contenido = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(ruta)));

            // 2. Imprimir el texto tal cual para ver si lo encuentra
            System.out.println("--- CONTENIDO CRUDO DEL JSON ---");
            System.out.println(contenido);
            System.out.println("--------------------------------");

            // 3. Si quieres que se vea un poco "ordenado" sin crear clases:
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Object jsonLindo = mapper.readValue(contenido, Object.class);
            String formateado = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonLindo);

            System.out.println("--- JSON FORMATEADO ---");
            System.out.println(formateado);

        } catch (Exception e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            e.printStackTrace();
        }
         **/
    }
}