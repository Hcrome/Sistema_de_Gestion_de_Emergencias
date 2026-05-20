package main.java.com.emergencias.model;

/**imports del jackson**/
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.emergencias.model.Hospital;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HospitalLoader {

    // La ruta queda fija aquí dentro
    private static final String RUTA_JSON = "src/main/resources/Hospital_list.json";
    private List<Hospital> listaHospitales;

    public HospitalLoader() {
        this.listaHospitales = new ArrayList<>();
    }

    /**
     * Este método realiza toda la operación: busca el archivo en la ruta fija,
     * usa Jackson para leerlo y llena la lista interna.
     */
    public void cargarDatos() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //* para que no de errores de lectura el JSON

        File archivo = new File(RUTA_JSON);

        try {
            if (archivo.exists()) {
                // Jackson mapea el archivo directamente a la lista de objetos Hospital
                this.listaHospitales = mapper.readValue(archivo, new TypeReference<List<Hospital>>(){});
                System.out.println("[HospitalLoader] Éxito: Se han cargado " + listaHospitales.size() + " hospitales.");
            } else {
                System.err.println("[Error] No se encontró el JSON en: " + archivo.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("[Error] Fallo al procesar el archivo JSON: " + e.getMessage());
            this.listaHospitales = new ArrayList<>(); // Evita nulos si falla
        }
    }

    // Getter para acceder a los datos
    public List<Hospital> getListaHospitales() {
        return this.listaHospitales;
    }

    // este metodo encuntra el hospital mas cercano.
    public Hospital encontrarMasCercano(double miLat, double miLon) {
        return listaHospitales.stream()
                .filter(Hospital::tieneCoordenadasValidas) // <-- metodo en Hospital.java
                .min((h1, h2) -> Double.compare(
                        h1.calcularDistanciaA(miLat, miLon),
                        h2.calcularDistanciaA(miLat, miLon)))
                .orElse(null);
    }

}
