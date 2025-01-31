package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class MapperHelper {

    private ObjectMapper objectMapper;

    /**
     * Constructor que inicializa el object mapper
     */
    public MapperHelper(){
        objectMapper = new ObjectMapper();
    }

    /**
     * Devuelve un JsonNode a partir de un File
     * @param file con contenido en formato Json
     * @return
     */
    public JsonNode getJsonNode(File file){
        canReadFile(file);
        JsonNode node = null;
        try {
            node = objectMapper.readTree(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("No se pudo obtener un JsonNode del archivo: " + file.getPath());
        }
        return node;
    }

    /**
     * Si el File no se puede leer, lanza un IllegalArgumentException
     * @param file
     */
    private void canReadFile(File file){
        if(!file.canRead()) throw new IllegalArgumentException("No se pudo leer el archivo: " + file.getPath());
    }

    public boolean isXMLValid(String response) {
        NodeHelper nodeHelper = new NodeHelper();
        nodeHelper.stringToNode(response);
        return true;
    }

}
