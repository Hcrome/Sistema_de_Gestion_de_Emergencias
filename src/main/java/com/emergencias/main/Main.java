package main.java.com.emergencias.main;
import main.java.com.emergencias.model.HospitalLoader;
import main.java.com.emergencias.model.Hospital;
import main.java.com.emergencias.controller.EmergencyManager;

public class Main {
    // Bajamos el umbral a 3 para que sea más fácil que salte la alerta en tu prueba
    private static final int UMBRAL_REQUERIDO = 3; 
    private static final String DESTINO_SERVICIO = "112 Emergencias Sanitarias";

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   SISTEMA DE DETECCIÓN DE EMERGENCIAS V2   ");
        System.out.println("============================================\n");

        EmergencyManager manager = new EmergencyManager(UMBRAL_REQUERIDO, DESTINO_SERVICIO);

        System.out.println("\nIniciando monitoreo de sensores...");
        manager.startSystem();

        System.out.println("\n============================================");
        System.out.println("           SIMULACIÓN FINALIZADA            ");
        System.out.println("============================================");
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