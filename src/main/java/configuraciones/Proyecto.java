package configuraciones;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Proyecto {
    @JsonProperty("proyecto")
    private String nombre;
    private String url;
    private String urlSoap;
    private List<Servicio> servicios;
    private String auth;

    public String getNombre() {
        return nombre;
    }

    @JsonProperty("proyecto")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlSoap() {
        return urlSoap;
    }

    public void setUrlSoap(String urlSoap) {
        this.urlSoap = urlSoap;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    /**
     * Obtiene un servicio buscando por nombre.
     * Si hay una propiedad de sistema con ese mismo nombre, devuelve una servicio nueva.
     * Devuelve {@code null} en caso de no encontrar una servicio con el nombre especificado
     *
     * @param nombre del servicio que se desea obtener
     * @return una instancia de {@link Servicio}
     */
    public Servicio getServicio(String nombre){
        if(System.getProperty(nombre) != null){
            return new Servicio().withNombre(nombre).withPath(System.getProperty(nombre));
        }
        Optional<Servicio> result = getServicios().stream().filter(coleccion -> nombre.equals(coleccion.getNombre())).findFirst();
        try {
            return result.get();
        } catch (Exception e) {
            throw new IllegalArgumentException("No se encontr\u00F3 el servicio '" + nombre + "' en la rama de datos");
        }
    }

    @JsonProperty("serviciosEstándar")
    private void setServiciosEstandar(List<Servicio> servicios) {
        setServicios(servicios);
    }

    public Proyecto withServicio(Servicio servicio) {
        setServicios(Arrays.asList(servicio));
        return this;
    }

    /**
     * Setea o actualiza la lista de servicios de esta configuración.
     *
     * Si ya hay servicios entonces se actualiza la lista.
     * Servicios nuevos son agregados mientras que servicios con mismo nombre son actualizados
     * @param servicios la lista de servicios nuevos.
     */
    public void setServicios(List<Servicio> servicios) {
        if (this.servicios == null) {
            this.servicios = servicios;
            return;
        }
          this.servicios = actualizarServicios(this.servicios, servicios);
    }

    /**
     * Unifica dos Listas de servicios en una sola.
     * Servicios nuevos son agregados. Servicios con el mismo nombre son actualizados.
     * Toma como base {@code serviciosOriginales} y actualiza las propiedades con valores distintos según {@code serviciosNuevos}
     * <p>por ejemplo: <br>
     *     servicio <b>original</b>: <code>nombre = Servicio1 , path = path/string </code><br> y
     *     servicio <b>nuevo</b>: <code>nombre = Servicio1, path = path/integer </code><br>
     *     resultan en un servicio <b>nueva unificada</b>:  <code>nombre = Servicio1, path = path/integer </code></p>
     * @param serviciosOriginales lista de servicios originales
     * @param serviciosNuevos lista de servicios candidatos para actualizar los servicios originales
     * @return lista nueva, resultado del merge entre {@code coleccionesOriginales} y {@code coleccionesNuevas}
     */
    public List<Servicio> actualizarServicios(List<Servicio> serviciosOriginales, List<Servicio> serviciosNuevos) {
        HashMap<String, Servicio> mapaNuevosValores = new HashMap<>();
        serviciosNuevos.stream()
                .filter(servicio -> serviciosOriginales.stream()
                        .anyMatch(servicioOriginal -> servicioOriginal.getNombre().equals(servicio.getNombre()))
                ).forEach(servicio -> {
                    mapaNuevosValores.put(servicio.getNombre(), servicio);
                });

        serviciosNuevos.removeAll(mapaNuevosValores.values());

        List<Servicio> updatedList = serviciosOriginales.stream()
                .map(servicio -> {
                            if (!mapaNuevosValores.containsKey(servicio.getNombre()))
                                return servicio;
                            Servicio servicioActualizado = mapaNuevosValores.get(servicio.getNombre());
                            if (servicioActualizado.getPath() != null)
                                servicio.setPath(servicioActualizado.getPath());
                            return servicio;
                        }
                ).collect(Collectors.toList());
        Stream<Servicio> mergedColecciones = Stream.concat(updatedList.stream(), serviciosNuevos.stream());
        return mergedColecciones.collect(Collectors.toList());
    }

    public Proyecto withNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public Proyecto withUrl(String url) {
        this.url = url;
        return this;
    }

    public String getAuth() { return auth; }

    public void setAuth(String auth) { this.auth = auth; }

    public Proyecto withAuth(String nombreDeItem) {
        this.auth = nombreDeItem;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proyecto proyecto = (Proyecto) o;

        if (!nombre.equals(proyecto.nombre)) return false;
        if (!Objects.equals(url, proyecto.url)) return false;
        if (!Objects.equals(urlSoap, proyecto.urlSoap)) return false;
        return Objects.equals(auth, proyecto.auth);
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "nombre:'" + nombre + '\'' +
                ", auth:'"+ auth + '\'' +
                ", url:'" + url + '\'' +
                ", urlSoap:'" + urlSoap + '\'' +
                '}';
    }
}
