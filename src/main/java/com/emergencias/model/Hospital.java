package main.java.com.emergencias.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Hospital {
    @JsonProperty("Código")
    private String codigo;
    @JsonProperty("Nombre")
    private String nombre;
    @JsonProperty("Dirección")
    private String direccion;
    @JsonProperty("C.P.")
    private String CP;
    @JsonProperty("Municipio")
    private String municipio;
    @JsonProperty("Pedanía")
    private String pedania;
    @JsonProperty("Teléfono")
    private String telefono;
    @JsonProperty("Fax")
    private String fax;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("URl Corta")
    private String urlshort;
    @JsonProperty("URL Real")
    private String urllong;
    private String tipo;
    @JsonProperty("Latitud")
    private String latitud;
    @JsonProperty("Longitud")
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


    public boolean tieneCoordenadasValidas() {
        try {
            if (latitud == null || longitud == null) return false;

            // Limpiamos espacios en blanco por si acaso
            String lat = latitud.trim();
            String lon = longitud.trim();

            if (lat.isEmpty() || lon.isEmpty()) return false;

            // Si el número es muy grande (como 4227950), no es una coordenada válida en grados
            // Una coordenada real suele estar entre -180 y 180
            double l1 = Double.parseDouble(lat);
            double l2 = Double.parseDouble(lon);

            if (Math.abs(l1) > 190 || Math.abs(l2) > 190) return false;

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // metodo para sacar la distancia del hospital mas cercano usando las coord. del usuario y del JSON (harvestine).
    // hay que evidenciar que en el JSON estan mal puestos y el numero de longitud figura como latitud y viceversa.
    public double calcularDistanciaA(double userLat, double userLon) {

        double hospitalLat = Double.parseDouble(this.latitud);
        double hospitalLon = Double.parseDouble(this.longitud);

        double earthRadius = 6371; // Kilómetros
        double dLat = Math.toRadians(hospitalLat - userLat);
        double dLon = Math.toRadians(hospitalLon - userLon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(hospitalLat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}



