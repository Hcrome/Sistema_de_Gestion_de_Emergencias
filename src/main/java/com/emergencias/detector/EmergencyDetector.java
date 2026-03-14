package main.java.com.emergencias.detector;

import main.java.com.emergencias.model.EmergencyEvent;
import main.java.com.emergencias.model.Hospital;
import main.java.com.emergencias.model.HospitalLoader;
import main.java.com.emergencias.model.UserData;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Clase principal para la detección y activación de la emergencia (Core 1).
 * Simula disparadores manuales y automáticos.
 */
public class EmergencyDetector {
    private final Scanner scanner;
    private final int UMBRAL_ACTIVACION; // Umbral de simulación para validación.

    public EmergencyDetector(int umbral) {
        this.scanner = new Scanner(System.in);
        this.UMBRAL_ACTIVACION = umbral;
    }

    /**
     * Simula la activación manual o automática y recopila datos.
     * param userData Datos del usuario que activa la emergencia.
     * return EmergencyEvent si se confirma la emergencia, null si se cancela.
     */
    public EmergencyEvent detectEvent(UserData userData) {
        System.out.println("--- Detección de Emergencia ---");
        System.out.println("Modo de Disparo: [1] Manual (Pulsación) | [2] Automático (Timer)");
        String modo = scanner.nextLine();

    // if else para deteccion de handle

        if ("1".equals(modo)) {
            return handleManualDetection(userData);
        } else if ("2".equals(modo)) {
            return handleAutomaticDetection(userData);
        } else {
            System.err.println("Modo no válido. Cancelando detección.");
            return null;
        }
    }

    // Constructor del motodo handleManualDetection

    private EmergencyEvent handleManualDetection(UserData userData) {
        System.out.print("Simular pulsación de botón de emergencia (E/e): ");
        String input = scanner.nextLine().trim();

        // If else para deteccion de activacion de emergencia

        if (input.equalsIgnoreCase("E")) {
            return gatherEventData(userData);
        } else {
            System.out.println("Pulsación no detectada. Proceso de detección cancelado.");
            return null;
        }
    }

    // Constructor del metodo handleAutomaticDetection

    private EmergencyEvent handleAutomaticDetection(UserData userData) {
        System.out.println("Activación automática simulada (e.g., por falta de respuesta del usuario).");

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        // Usamos un objeto para saber si ha habido cancelación
        final boolean[] isCancelled = {false};

        System.out.println("Esperando 5 segundos para confirmación de usuario. Escribe 'OK' para CANCELAR.");

        // Tarea que se ejecuta si no hay cancelación en 5 segundos
        Runnable task = () -> {
            if (!isCancelled[0]) {
                System.out.println("\n[AUTOMÁTICO] Tiempo agotado. Se asume emergencia. Iniciando recopilación de datos.");
                // La recopilación de datos y creación de evento se realiza en el hilo principal
            }
            scheduler.shutdown();
        };

        // Iniciar la tarea después de 5 segundos
        scheduler.schedule(task, 5, TimeUnit.SECONDS);

        // Esperar la entrada de cancelación en el hilo principal
        String cancel = scanner.nextLine().trim();

        if (cancel.equalsIgnoreCase("OK")) {
            System.out.println("Emergencia automática cancelada por el usuario.");
            isCancelled[0] = true;
            scheduler.shutdownNow(); // Detener el timer
            return null;
        }

        // Si llegamos aquí y no fue cancelado por 'OK', necesitamos esperar que el scheduler termine
        // antes de proceder a la recopilación de datos.
        try {
            scheduler.awaitTermination(6, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }

        // Ahora, comprobamos el estado de la cancelación
        if (isCancelled[0]) {
            return null; // Si fue cancelado dentro de los 5s (aunque ya lo hicimos arriba, es una doble verificación)
        } else {
            // Si el temporizador finalizó y no fue cancelado, procedemos a recopilar los datos.
            // Aquí se llamará a gatherEventData, que pedirá el tipo de emergencia.
            return gatherEventData(userData);
        }
    }

    // metodo para registro de tipo de emergencia y gravedad

    private EmergencyEvent gatherEventData(UserData userData) {
        System.out.println("\n--- REGISTRO DE UBICACIÓN ---");
        System.out.println("[1] Introducir Dirección Manual");
        System.out.println("[2] Usar Coordenadas GPS (Buscar Hospital más cercano)");
        System.out.print("Seleccione una opción: ");

        String opc = scanner.nextLine();
        String ubicacionFinal = "";

        if (opc.equals("2")) {
            try {
                System.out.print("Introduce tu Latitud (ej. 38.0): ");
                double lat = Double.parseDouble(scanner.nextLine());
                System.out.print("Introduce tu Longitud (ej. -1.0): ");
                double lon = Double.parseDouble(scanner.nextLine());

                HospitalLoader loader = new HospitalLoader();
                loader.cargarDatos();

                // Buscamos el hospital más cercano
                Hospital cercano = loader.encontrarMasCercano(lat, lon);

                if (cercano != null) {
                    double dist = cercano.calcularDistanciaA(lat, lon);
                    ubicacionFinal = String.format("Cerca de: %s (%s) a %.2f km",
                            cercano.getNombre(), cercano.getMunicipio(), dist);
                    System.out.println("\n[GPS] Ubicación detectada con éxito.");
                } else {
                    System.out.println("[!] No se encontraron hospitales con coordenadas válidas.");
                    ubicacionFinal = "Coordenadas: " + lat + ", " + lon;
                }
            } catch (NumberFormatException e) {
                System.err.println("[Error] Formato de coordenadas inválido. Cambiando a manual...");
                System.out.print("Introduzca dirección manualmente: ");
                ubicacionFinal = scanner.nextLine();
            }
        } else {
            System.out.print("Introduzca la dirección de la emergencia: ");
            ubicacionFinal = scanner.nextLine();
        }

        // Pedimos el tipo de emergencia (Solo UNA vez)
        System.out.print("Tipo de Emergencia (Sanitaria, Accidente, Fuego...): ");
        String tipo = scanner.nextLine();

        try {
            // Validamos la gravedad antes de crear el evento
            if (validateSeverity()) {
                System.out.println("Validación de gravedad exitosa. Creando evento.");
                // IMPORTANTE: Usamos ubicacionFinal que es la que tiene los datos del GPS o Manual
                return new EmergencyEvent(tipo, ubicacionFinal, userData);
            } else {
                System.out.println("Validación de gravedad fallida. Falso positivo evitado.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error en la validación: " + e.getMessage());
            return null;
        }
    }

    /**
     * Valida la gravedad simulando una verificación.
     * return true si la emergencia supera el umbral.
     * (̶t̶h̶r̶o̶w̶s̶ ̶I̶l̶l̶e̶g̶a̶l̶A̶r̶g̶u̶m̶e̶n̶t̶E̶x̶c̶e̶p̶t̶i̶o̶n̶ ̶s̶i̶ ̶l̶a̶ ̶e̶n̶t̶r̶a̶d̶a̶ ̶n̶o̶ ̶e̶s̶ ̶u̶n̶ ̶n̶ú̶m̶e̶r̶o̶.̶)̶
     * Sustituimos el IllegalArgumentException y añadimos un control de excepciones propio
     */
    public boolean validateSeverity() {
        int intentos = 0;
        while (intentos < 3) {
            System.out.printf("Simulación de gravedad: Introduce un valor de impacto (mínimo %d para activar): ", UMBRAL_ACTIVACION);
            String input = scanner.nextLine();
            try {
                int impacto = Integer.parseInt(input);
                return impacto >= UMBRAL_ACTIVACION;
            } catch (NumberFormatException e) {
                intentos++;
                System.out.println("Entrada inválida. Intente de nuevo (" + (3 - intentos) + " intentos restantes). ");
            }
        }
        System.err.println("Demasiados intentos fallidos. Por seguridad, se cancela la validación"); //IllegalArgumentException sustituido por system.err.println
        return false; //integridad de sistema continua, cancela evento dudoso.


    }
}