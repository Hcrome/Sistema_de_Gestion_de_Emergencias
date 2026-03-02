package main.java.com.emergencias.model;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
* Esta clse la hice antes por equivocacion, pero ya esta hecha,
 * asi que la dejo para comodidad del compi.
**/


public class Hospital {
    private String codigo;
    private String nombre;
    private String direccion;
    private String CP;
    private String municipio;
    private String pedania;
    private String telefono;
    private String fax;
    private String email;
    private String urlshort;
    private String urllong;
    private String tipo;
    private String latitud;
    private String longitud;

    public Hospital() {}

    /**
     * getters y setters (añado todos los parametros de la lista por si acaso,
     * ademas con el autocompletado es muy comodo)
     **/
    public String getCodigo() {return codigo;}
    public void setCodigo(String codigo) {this.codigo = codigo;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getDireccion() {return direccion;}
    public void setDireccion(String direccion) {this.direccion = direccion;}

    public String getCP() {return CP;}
    public void setCP(String CP) {this.CP = CP;}

    public String getMunicipio() {return municipio;}
    public void setMunicipio(String municipio) {this.municipio = municipio;}

    public String getPedania() {return pedania;}
    public void setPedania(String pedania) {this.pedania = pedania;}

    public String getTelefono() {return telefono;}
    public void setTelefono(String telefono) {this.telefono = telefono;}

    public String getFax() {return fax;}
    public void setFax(String fax) {this.fax = fax;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getUrlshort() {return urlshort;}
    public void setUrlshort(String urlshort) {this.urlshort = urlshort;}

    public String getUrllong() {return urllong;}
    public void setUrllong(String urllong) {this.urllong = urllong;}

    public String getTipo() {return tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}

    public String getLatitud() {return latitud;}
    public void setLatitud(String latitud) {this.latitud = latitud;}

    public String getLongitud() {return longitud;}
    public void setLongitud(String longitud) {this.longitud = longitud;}

    }



