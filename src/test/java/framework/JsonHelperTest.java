package framework;

import com.fasterxml.jackson.databind.JsonNode;
import static helpers.ObjectMapperHelper.*;
import static helpers.JsonNodeHelper.*;
import static java.lang.String.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



public class JsonHelperTest {

    @Test
    @DisplayName("Should get the value of one specific node like String")
    public void shouldGetTheValueOfOneSpecificNodeLikeString(){
        String nodeName = "nodo1";
        String expectedNodeValue = "valor1";
        String jsonString = "{\"nodo1\":\"valor1\",\"nodo2\":{\"subnodo1\":\"subvalor1\",\"subnodo2\":\"subvalor2\"}}";
        JsonNode jsonNode = createJsonNodeFromString(jsonString);
        String nodeValue = getStringValueFromJson(nodeName,jsonNode);
        Assertions.assertEquals(expectedNodeValue,nodeValue,"The node values are differents");
    }

    @Test
    @DisplayName("Should remove one specific node")
    public void shouldRemoveOneSpecificNode(){
        String nodeName = "nodo2/subnodo1";
        String expectedJsonString = "{\"nodo1\":\"valor1\",\"nodo2\":{\"subnodo2\":\"subvalor2\"}}";
        String actualJsonString = "{\"nodo1\":\"valor1\",\"nodo2\":{\"subnodo1\":\"subvalor1\",\"subnodo2\":\"subvalor2\"}}";
        JsonNode jsonNode = createJsonNodeFromString(actualJsonString);
        jsonNode = removeNodeFromJson(nodeName,jsonNode);
        Assertions.assertEquals(expectedJsonString,valueOf(jsonNode),"The JsonNode are differents");
    }

    @Test
    @DisplayName("Should set one specific node in a existing JsonNode")
    public void shouldSetOneSpecificNodeInAExistingJsonNode(){
        String nodeName = "nodo3";
        String nodeValue = "valor3";
        String expectedJsonString = "{\"nodo1\":\"valor1\",\"nodo2\":{\"subnodo1\":\"subvalor1\",\"subnodo2\":\"subvalor2\"},\"nodo3\":\"valor3\"}";
        String actualJsonString = "{\"nodo1\":\"valor1\",\"nodo2\":{\"subnodo1\":\"subvalor1\",\"subnodo2\":\"subvalor2\"}}";
        JsonNode jsonNode = createJsonNodeFromString(actualJsonString);
        jsonNode = setNodeInJson(nodeName,nodeValue,jsonNode);
        Assertions.assertEquals(expectedJsonString,valueOf(jsonNode),"The JsonNode are differents");
    }

    @Test
    @DisplayName("Should set one specific node in a non-existing JsonNode")
    public void shouldSetOneSpecificNodeInANonExistingJsonNode(){
        String nodeName = "nodo3";
        String nodeValue = "valor3";
        String expectedJsonString = "{\"nodo3\":\"valor3\"}";
        JsonNode jsonNode = setNodeInJson(nodeName,nodeValue);
        Assertions.assertEquals(expectedJsonString,valueOf(jsonNode),"The JsonNode are differents");
    }

    @Test
    @DisplayName("Should set a node in an ArrayNode of a JsonNode")
    public void shouldSetNodeInAnArrayNode(){
        String node = "nodo2/subnodo2";
        String nodeValue = "subvalor3";
        String jsonString = "{\"nodo1\":\"valor1\",\"nodo2\":{\"subnodo1\":\"subvalor1\",\"subnodo2\":\"subvalor2\"}}";
        JsonNode jsonNode = createJsonNodeFromString(jsonString);
        setNodeInJson(node, nodeValue, jsonNode);
        Assertions.assertEquals(nodeValue, jsonNode.at("/"+node).asText(),"The JsonNode values are differents");
    }

    @Test
    @DisplayName("Should set a node in an Array of Nodes of a JsonNode")
    public void shouldSetNodeInAnArrayOfNodes(){
        String node = "subnodo1";
        String nodeValue = "subvalor5";
        String jsonString = "{\"nodo1\":\"valor1\",\"nodo2\":[{\"subnodo1\":\"subvalor1\",\"subnodo2\":\"subvalor2\"},{\"subnodo1\":\"subvalor3\",\"subnodo2\":\"subvalor4\"}]}";
        JsonNode jsonNode = createJsonNodeFromString(jsonString);
        setValueInArrayJsonNode(node, nodeValue, jsonNode.at("/nodo2"), 1);

        Assertions.assertEquals(nodeValue, jsonNode.at("/nodo2").get(1).at("/"+node).asText(),"JsonNode values are differents");
    }

}
