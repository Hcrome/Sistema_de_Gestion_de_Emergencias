package main.java.com.emergencias.model;
// clase POJO, recojida de datos para la instruccion
public class Instruction {
    private int id;
    private String titulo;
    private String descripcion;

    public Instruction() {}

    //Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    // presentacion de datos del JSON
    @Override
    public String toString() {
        return "[" + id + "]" + titulo + "\n  " + descripcion;
    }
}
