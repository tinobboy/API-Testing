package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import configuraciones.Configuracion;
import configuraciones.Proyecto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class ConfigHelper {

    public static final String CONFIGURACION = "configuracion";
    private static Logger LOGGER = Logger.getLogger("ConfigHelper");
    private static String defaultSettingsFile = "src/test/resources/defaults.yml";
    private static Configuracion configuracion;

    static {
        try {
            setConfiguracion();
        }catch (ConfiguracionException e) {
            throw e;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Agregar propiedad "resultsDir" al modelo de proyecto
    public static String getresultsDir(){
        return System.getProperty("resultsDir", "build/allure-results");
    }

    /**
     * Devuelve las configuraciones
     * @return la instancia de configuración
     */
    public static Configuracion getConfiguracion() {
        return configuracion;
    }

    /**
     * Establece la configuración de proyectos
     * @throws IOException si hay un error en la obtención del inputstream de configuración
     */
    public static void setConfiguracion() throws IOException {
        List<HashMap<String, Object>> configurationInputs = getArchivosConfiguracionYFiltros();
        if (getDefaultSettingsFile().exists()){
            HashMap<String, Object> map = new HashMap<>();
            map.put("filePath", getDefaultSettingsFile());
            map.put("filter", "default");
            configurationInputs.add(0,map);
        } else {
            LOGGER.warning("No se pudo encontrar el archivo con las configuraciones por defecto: " + getDefaultSettingsFile().getPath());
        }

        for (HashMap<String, Object> configItem : configurationInputs){
            ConfigHelper.configuracion = readConfiguracion(new FileInputStream((File) configItem.get("filePath")), (String) configItem.get("filter"), ConfigHelper.getConfiguracion());
        }
        ConfigHelper.validateDirInputFiles(ConfigHelper.configuracion);
        ConfigHelper.validateUrl((ConfigHelper.configuracion));
    }

    /**
     * Crea un objeto de configuración nuevo según parametro.
     *
     * @param configuracion stream con la información en formato yaml para armar el objeto de {@link Configuracion}
     * @param filter nombre del nodo de configuración
     * @return un objeto de {@link Configuracion} nuevo
     * @throws IOException si hay un error en la obtención del inputstream de configuración
     * @see #readConfiguracion(InputStream, String, Configuracion)
     */
    public static Configuracion readConfiguracion(InputStream configuracion, String filter) throws IOException {
        return readConfiguracion(configuracion, filter, null);
    }

    /**
     * Crea un objeto de configuración según parametro. Actualiza la configuración base
     *
     * @param configuracionNueva stream con la información de la configuración nueva y/o actualizada
     * @param filter nombre del nodo de configuración
     * @param configuracionBase objeto preexistente que se actualizará con la información segun {@code configuracionNueva}
     * @return un objeto de {@link Configuracion} actualizado
     * @throws IOException en caso de que haya problemas con la lectura de los streams de configuración
     * @see #readConfiguracion(InputStream, String, Configuracion)
     */
    public static Configuracion readConfiguracion(InputStream configuracionNueva, String filter, Configuracion configuracionBase) throws IOException {
        byte[] configuracionNuevaBytes = new byte[configuracionNueva.available()];
        configuracionNueva.read(configuracionNuevaBytes);
        YAMLFactory yamlFactory = new YAMLFactory();
        YAMLParser parser = yamlFactory.createParser(configuracionNuevaBytes);
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader settingsReader;

        if(configuracionBase == null) {
            configuracionBase = new Configuracion();
            mapper.readerFor(Configuracion.class);
        }
        settingsReader = mapper.readerForUpdating(configuracionBase);
        if (filter == null){
            return settingsReader.readValue(parser);
        }else {
            JsonNode node = settingsReader.readTree(parser);
            settingsReader.treeToValue(node.at("/default"), Configuracion.class);
            settingsReader.treeToValue(node.at("/"+filter), Configuracion.class);
            return configuracionBase;
        }
    }

    private static File getDefaultSettingsFile() {
        return new File(defaultSettingsFile);
    }

    public static void setDefaultSettingsFile(String filePath) {
        ConfigHelper.defaultSettingsFile = filePath;
    }

    /**
     * Obtiene las rutas de los archivos de configuracion y el nombre de la configuracion si existe.
     * Hace un split: {@code "jenkins.settings.yml;/datos/desa.settings.yml:desarrollo"} <br>
     * Se convierte en <pre>[ <br>  { filePath:"jenkins.settings.yml" },<br>  { filePath: "/datos/desa.settings.yml", filter: "desarrollo" } <br>] </pre>
     * @return hasmap con la ruta del archivo yml y nombre de la configuración a leer
     */
    private static List<HashMap<String, Object>> getArchivosConfiguracionYFiltros(){
        List<HashMap<String, Object>> result = new ArrayList<>();
        String rawProperty = System.getProperty(CONFIGURACION, System.getenv(CONFIGURACION));
        if (rawProperty == null){
            LOGGER.warning("No se encontro una propiedad de sistema o variable de entorno 'configuracion'. Se aplicaran los valores por defecto.");
            return result;
        }
        String[] filesAndFilters = rawProperty.split(";");
        for (String fileAndFilter : filesAndFilters) {
            String[] parts = fileAndFilter.split("#",2);
            HashMap<String, Object> map = new HashMap<>();
            map.put("filePath", new File(parts[0]));
            if(parts.length > 1){
                map.put("filter", parts[1]);
            }
            result.add(map);
        }
        return result;
    }

    /**
     * Lanza una excepción si el valor de dirInputFiles de una configuración es omitido o esta vacio.
     * @param configuracionEsperada objeto prexistente de clase Configuracion.
     * @throws ConfiguracionException en caso de que el dirInputFile de la configuracionEsperada sea null o vacio.
     */
    public static void validateDirInputFiles(Configuracion configuracionEsperada) throws ConfiguracionException {
        if ( configuracionEsperada.getDirInputFiles()==null || configuracionEsperada.getDirInputFiles().toString().isEmpty())
            throw new ConfiguracionException("validateDirInputFile(): el valor de dirInputFiles es nulo o vacio(\"\")");
    }

    /**
     * Lanza una excepción si el valor de Url de una configuración con colecciones  es omitido o esta vacío.
     * @param configuracionEsperada objeto prexistente de clase Configuracion.
     * @throws ConfiguracionException en caso de que la URL de la configuracionEsperada sea null o vacío.
     */
    public static void validateUrl(Configuracion configuracionEsperada) {
        List<Proyecto> proyectos = configuracionEsperada.getProyectos();
        for (Proyecto proyecto: proyectos) {
            if(proyecto.getUrl() == null || proyecto.getUrl().isEmpty())
                throw new ConfiguracionException("validateUrl():  el valor URL es nulo o vacío(\"\")");
        }
    }
}
