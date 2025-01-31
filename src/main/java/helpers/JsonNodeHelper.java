package helpers;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static helpers.StringHelper.*;
import static helpers.ObjectMapperHelper.*;

public class JsonNodeHelper {

    public static JsonNode removeNodeFromJson(String node, JsonNode jsonNode){
        JsonPointer jsonPointer = JsonPointer.compile(formatText("/%s",node));
        ((ObjectNode) jsonNode.at(jsonPointer.head())).remove(jsonPointer.last().getMatchingProperty());
        return jsonNode.at("");
    }

    public static String getStringValueFromJson(String node, JsonNode jsonNode){
        return jsonNode.at(JsonPointer.compile(formatText("/%s",node))).asText();
    }

    public static JsonNode setNodeInJson(String nodeName,Object nodeValue){
        JsonNode jsonNodeWithValue = createJsonNode(nodeValue);
        JsonPointer jsonPointer=JsonPointer.compile(formatText("/%s",nodeName));
        JsonNode newJsonNode = createEmptyJsonNode();
        ((ObjectNode) newJsonNode.at("")).set(jsonPointer.getMatchingProperty(), jsonNodeWithValue);
        return newJsonNode.at("");
    }

    public static JsonNode setNodeInJson(String nodeName,Object nodeValue,JsonNode jsonNode){
        JsonPointer jsonPointer=JsonPointer.compile(formatText("/%s",nodeName));
        JsonNode jsonNodeWithValue = createJsonNode(nodeValue);
        ((ObjectNode) jsonNode.at(jsonPointer.head())).set(jsonPointer.last().getMatchingProperty(), jsonNodeWithValue);
        return jsonNode.at("");
    }

    public static JsonNode setValueInArrayJsonNode(String nodeName, Object nodeValue, JsonNode jsonNode, Integer index){
        ((ObjectNode) jsonNode.get(index)).put(nodeName, String.valueOf(nodeValue));
        return jsonNode.at("");
    }
}
