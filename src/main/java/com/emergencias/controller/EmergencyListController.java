package main.java.com.emergencias.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import main.java.com.emergencias.model.Instruction;

import java.io.File;
import java.util.List;

public class EmergencyListController {

    @FXML private TextArea txtInstrucction;

    @FXML
    public void initialize() {

        // Busca el json, deserializa los objetos y recorre la lista formando un texto que se vuelca en textarea.
        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File("src/main/resources/instrucciones.json");

            if (jsonFile.exists()) {
                List<Instruction> lista = mapper.readValue(jsonFile,
                        new TypeReference<List<Instruction>>(){});

                StringBuilder sb = new StringBuilder("=== GUÍA DE EMERGENCIAS ===\n\n");
                for (Instruction i : lista) {
                    sb.append(i.toString()).append("\n----------------------\n");
                }
                txtInstrucction.setText(sb.toString());

            } else {
                txtInstrucction.setText("Error: No se encontró el archivo en "
                        + jsonFile.getAbsolutePath());
            }

        } catch (Exception e) {
            txtInstrucction.setText("Error al cargar instrucciones: " + e.getMessage());
        }
    }
}
