package configuraciones;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.logging.Logger;

/**
 * <h1>Servicio</h1>
 * Clase que se utiliza para mapear los servicios leidos en el archivo configuracion YML
 * @author mlarotonda
 * @since 21-04-2022
 * @version 1.0.0
 * @see Configuracion
 * @see Proyecto
 */
public class Servicio {
    @JsonProperty("servicio")
    private String nombre;
    private String path;
    private String pathSoap;
    private boolean ignorar = false;
    private String razon;

    public String getRazon() { return razon; }

    public void setRazon(String razon) { this.razon = razon; }

    public boolean isIgnorar() {
        return ignorar;
    }

    public void setIgnorar(boolean ignorar, String razon){
        if(ignorar && razon == null){
            Logger.getLogger("ServicioLogger").warning(getNombre() + ": Se ignora el servicio sin proveer una razón");
            razon = "No se provee una razón";
        }
        this.ignorar = ignorar;
        setRazon(razon);

    }

    public String getPath() {
        path = System.getProperty(getNombre(),path);
        if (path==null){
            throw new IllegalArgumentException("No se encontr\u00F3 el path '" + getNombre() + "' en la rama de datos");
        }
            return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathSoap() {
        pathSoap = System.getProperty(getNombre(), pathSoap);
        if (pathSoap==null){
            throw new IllegalArgumentException("No se encontr\u00F3 el path '" + getNombre() + "' en la rama de datos");
        }
            return pathSoap;
    }

    public void setPathSoap(String pathSoap) { this.pathSoap = pathSoap; }

    public String getNombre() {
        return nombre;
    }

    public Servicio withNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public Servicio withPath(String path) {
        this.path = path;
        return this;
    }

    public Servicio withPathSoap(String pathSoap) {
        this.pathSoap = pathSoap;
        return this;
    }

    public Servicio withIgnorar(boolean ignorar, String razon) {
        this.ignorar = ignorar;
        this.razon = razon;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servicio that = (Servicio) o;
        return nombre.equals(that.nombre) && path.equals(that.path) && pathSoap.equals(that.pathSoap);
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "nombre:'" + nombre + '\'' +
                ", path:'" + path + '\'' +
                ", pathSoap:'" + pathSoap + '\'' +
                ", ignorar:'" + ignorar + '\'' +
                ", razon:'" + razon + '\'' +
                '}';
    }
}
