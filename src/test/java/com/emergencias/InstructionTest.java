package test.java.com.emergencias;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import main.java.com.emergencias.model.Instruction;

public class InstructionTest {

    @Test
    void testCreacionInstruccion() {
        // Comprobamos que el objeto se crea y guarda datos bien
        Instruction ins = new Instruction();
        ins.setId(10);
        ins.setTitulo("Test de Emergencia");

        assertEquals(10, ins.getId());
        assertEquals("Test de Emergencia", ins.getTitulo());
    }

    @Test
    void testToStringFormato() {
        // Comprobamos que el toString no devuelva algo vacío
        Instruction ins = new Instruction();
        ins.setTitulo("Protocolo");
        ins.setDescripcion("Pasos a seguir");

        assertNotNull(ins.toString());
        assertTrue(ins.toString().contains("Protocolo"));
    }
}