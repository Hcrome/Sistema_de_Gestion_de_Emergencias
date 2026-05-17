package main.java.com.emergencias.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.emergencias.model.Instruction;
import java.io.File;
import java.util.List;
import java.util.Scanner;

public class UNUSEInstructionManager {
    private List<Instruction> instrucciones;
    private static final String RUTA_JSON = "src/main/java/resources/instrucciones.json";

    public UNUSEInstructionManager() {
        cargarInstrucciones();
    }
    // cargador de JSON para su posterior consulta
    private void cargarInstrucciones() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.instrucciones = mapper.readValue(new File(RUTA_JSON), new TypeReference<List<Instruction>>(){});
        } catch (Exception e) {
            System.err.println("Error cargando instrucciones: " + e.getMessage());
        }
    }
    // metodo para la consulta ded protocolos de emergencia
    // itera sobre el JSON y muestra el tipo de demergencia y su ID para la seleccion.
    public void mostrarMenuInstrucciones() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- GUÍA DE PRIMEROS AUXILIOS ---");
        for (Instruction ins : instrucciones) {
            System.out.println(ins.getId() + ". " + ins.getTitulo());
        }
        System.out.print("Seleccione una opción (0 para salir): ");
        int opcion = sc.nextInt();

        if (opcion != 0) {
            buscarYMostrar(opcion);
        }
    }
    // Filtro para el JSON, busca por id usando Stream.
    private void buscarYMostrar(int id) {
        instrucciones.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Opción no válida.")
                );
    }
}
