package helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import static helpers.StringHelper.*;

public class ObjectMapperHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode createEmptyJsonNode(){
       return createJsonNodeFromString("{}");
    }

    public static JsonNode createJsonNode(Object value){
            return  objectMapper.valueToTree(value);
    }

    public static JsonNode createJsonNodeFromString(String jsonString){
        try {
            return objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(formatText("Cant create a Empty JsonNode. %s is a invalid String",jsonString));
        }
    }

    public static JsonNode getJsonNodeFromFile(File file){
        canReadFile(file);
        JsonNode node = null;
        try {
            node = objectMapper.readTree(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("No se pudo obtener un JsonNode del archivo: " + file.getPath());
        }
        return node;
    }

    private static void canReadFile(File file){
        if(!file.canRead()) throw new IllegalArgumentException("No se pudo leer el archivo: " + file.getPath());
    }

    public static boolean isJSONValid(String jsonString ) {
        try {
            objectMapper.readTree(jsonString);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }

    }

}
