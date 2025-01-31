package configuraciones;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Configuracion {
    private static Logger LOGGER = Logger.getLogger("Configuracion");
    private String id;
    private File dirInputFiles;
    private List<Proyecto> proyectos;

    public Configuracion() { this("default"); }

    public Configuracion(String id) { this.id = id; }

    public String getId() {
        return id;
    }

    public File getDirInputFiles() {
        if(System.getProperty("dirInputFiles") != null)
            return new File(System.getProperty("dirInputFiles"));
        return dirInputFiles;
    }

    public void setDirInputFiles(String value) {
        this.dirInputFiles = new File(value);
    }

    public Configuracion withDirInputFiles(String value) {
        setDirInputFiles(value);
        return this;
    }

    public List<Proyecto> getProyectos() { return proyectos; }

    public Configuracion withProyecto(Proyecto proyecto){
        setProyectos(Arrays.asList(proyecto));
        return this;
    }

    public Configuracion withProyectos(List<Proyecto> proyectos){
        setProyectos(proyectos);
        return this;
    }

    /**
     * Establece o actualiza la lista de proyectos.
     * @param proyectos la lista de proyectos nuevos o con valores nuevos
     * @see #actualizarProyectos(List, List)
     */
    public void setProyectos(List<Proyecto> proyectos) {
        if(this.proyectos == null) {
            this.proyectos = proyectos;
            return;
        }
        this.proyectos = actualizarProyectos(this.proyectos, proyectos);
    }

    /**
     * Unifica dos listas de proyectos.
     * Actualiza proyectos existentes con el mismo nombre.
     * Agrega proyectos nuevos que no existen en la lista original.
     * No elimina proyectos de la lista.
     *
     * @param proyectosOriginales lista original de proyectos
     * @param proyectosNuevos lista con potencialmente proyectos adicionales y/o valores nuevos para proyectos existentes
     * @return lista de proyectos actualizados
     * @see #setProyectos(List)
     */
    private List<Proyecto> actualizarProyectos(List<Proyecto> proyectosOriginales, List<Proyecto> proyectosNuevos) {
        // coleccionar proyectos que se repitan en la lista original y la nueva. Candidatos para la actualización de valores
        HashMap<String, Proyecto> mapaNuevosValores = new HashMap<>();
        proyectosNuevos.stream()
                .filter(proyecto -> proyectosOriginales.stream()
                            .anyMatch(proyectoOriginal -> Objects.equals(proyectoOriginal.getNombre(), proyecto.getNombre()))
                ).forEach(proyecto -> mapaNuevosValores.put(proyecto.getNombre(), proyecto));

        // Reducir la lista de proyectos nuevos para que tenga solo los elementos completamente nuevos
        proyectosNuevos.removeAll(mapaNuevosValores.values());

        // Crear lista de proyectos con valores actualizados. Se modifica el original
        List<Proyecto> updatedlist = proyectosOriginales.stream()
                .map(proyecto -> {
                            // nombre, url, colecciones / coleccionesEstándar
                            if (!mapaNuevosValores.containsKey(proyecto.getNombre())){
                                return proyecto;
                            }
                            Proyecto proyectoActualizado = mapaNuevosValores.get(proyecto.getNombre());
                            if (proyectoActualizado.getUrl() != null){
                                proyecto.setUrl(proyectoActualizado.getUrl());
                            }
                            if (proyectoActualizado.getUrlSoap() != null){
                                proyecto.setUrlSoap(proyectoActualizado.getUrlSoap());
                            }
                            if (proyectoActualizado.getAuth() != null){
                                proyecto.setAuth(proyectoActualizado.getAuth());
                            }
                            if (proyectoActualizado.getServicios() != null){
                                proyecto.setServicios(proyectoActualizado.getServicios());
                            }
                            return proyecto;
                        }
                ).collect(Collectors.toList());
        Stream<Proyecto> mergedProyectos = Stream.concat(updatedlist.stream(), proyectosNuevos.stream());
        return mergedProyectos.collect(Collectors.toList());
    }

    public Proyecto getProyecto(String nombre) {
        Optional<Proyecto> result = getProyectos().stream().filter(proyecto -> nombre.equals(proyecto.getNombre())).findFirst();
        return result.orElse(null);
    }
}
