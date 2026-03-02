package main.java.com.emergencias.main;

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
    }
}