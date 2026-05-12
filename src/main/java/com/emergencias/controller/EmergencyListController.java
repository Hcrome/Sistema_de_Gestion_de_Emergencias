package main.java.com.emergencias.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import main.java.com.emergencias.model.Instruction;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class EmergencyListController {
    private Parent root;

    public EmergencyListController(Parent root, Stage stage) {
        this.root = root;
        // Forzamos un pequeño retraso para que la UI se asiente si es necesario
        javafx.application.Platform.runLater(this::cargarDatosComoSea);
    }

    private void cargarDatosComoSea() {
        // 1. BUSCAR EL TEXTAREA (Intentamos lookup normal y luego manual)
        TextArea txt = (TextArea) root.lookup("#txtInstrucction");

        if (txt == null) {
            // Plan B: Si el lookup falla, buscamos a mano entre todos los hijos
            txt = buscarTextAreaManualmente(root);
        }

        if (txt == null) {
            System.err.println("CRÍTICO: No hay ningún TextArea en este FXML.");
            return;
        }

        // 2. LEER EL JSON
        try {
            ObjectMapper mapper = new ObjectMapper();

            // USAMOS LA MISMA LÓGICA QUE EN EL FXML (Ruta física)
            File jsonFile = new File("src/main/resources/instrucciones.json");

            if (jsonFile.exists()) {
                // Si el archivo existe físicamente, Jackson lo lee directo del File
                List<Instruction> lista = mapper.readValue(jsonFile, new TypeReference<List<Instruction>>(){});

                StringBuilder sb = new StringBuilder("=== GUÍA DE EMERGENCIAS (Carga Física) ===\n\n");
                for (Instruction i : lista) {
                    sb.append(i.toString()).append("\n----------------------\n");
                }
                txt.setText(sb.toString());
                System.out.println("DEBUG: JSON cargado desde ruta física OK.");
            } else {
                // Si no existe el archivo, usamos el PLAN DE RESCATE (Hardcoded)
                // Así la interfaz NUNCA saldrá vacía
                txt.setText("=== GUÍA DE EMERGENCIAS ===\n\n" +
                        "[1] Maniobra de Heimlich\nColóquese detrás de la persona. Presione hacia adentro y arriba.\n\n" +
                        "[2] RCP Básica\n30 compresiones fuertes y rápidas seguidas de 2 insuflaciones.\n\n" +
                        "[3] Accidente de Moto\nNO retire el casco. Inmovilice el cuello.\n\n" +
                        "(Nota: Archivo JSON no encontrado en " + jsonFile.getAbsolutePath() + ")");
            }
        } catch (Exception e) {
            txt.setText("Error en el proceso: " + e.getMessage());
        }
    }

    // Método de emergencia para encontrar el componente si el ID falla
    private TextArea buscarTextAreaManualmente(Parent parent) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof TextArea) return (TextArea) node;
            if (node instanceof Parent) {
                TextArea encontrada = buscarTextAreaManualmente((Parent) node);
                if (encontrada != null) return encontrada;
            }
        }
        return null;
    }
}