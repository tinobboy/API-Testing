package framework;

import com.fasterxml.jackson.databind.JsonNode;
import static helpers.ObjectMapperHelper.*;
import static helpers.StringHelper.*;
import static java.lang.String.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;


public class ObjectMapperHelperTest {

    @Test
    @DisplayName("Should create a empty JsonNode")
    public void ShouldCreateAEmptyJsonNode(){
        String expectedString = "{}";
        JsonNode jsonNode = createEmptyJsonNode();
        assertTrue(jsonNode.isEmpty(),"The JsonNode is not empty");
        assertFalse(jsonNode.isValueNode(),"the JsonNode must not be a value node");
        assertEquals(expectedString,valueOf(jsonNode),"The JsonNodes have differentes values");
    }

    @Test
    @DisplayName("Should create JsonNode with a value")
    public void ShouldCreateJsonNodeWithAValue(){
        Object actualValue = 6;
        JsonNode jsonNode = createJsonNode(actualValue);
        assertTrue(jsonNode.isEmpty(),"The JsonNode is not Empty");
        assertTrue(jsonNode.isValueNode(),"The JsonNode must be a value node");
        assertEquals(valueOf(actualValue),jsonNode.asText(),"The JsonNodes have differentes values");
    }

    @Test
    @DisplayName("Should create JsonNode using a String")
    public void ShouldCreateJsonNodeUsingAString(){
        String actualJsonString = "{\"nodo1\":\"valor1\",\"nodo2\":{\"subnodo1\":\"subvalor1\",\"subnodo2\":\"subvalor2\"}}";
        JsonNode jsonNode = createJsonNodeFromString(actualJsonString);
        assertEquals(actualJsonString,valueOf(jsonNode),"The JsonNodes have differentes values");
    }

    @Test
    @DisplayName("Should throw exception when trying create JsonNode using a invalid String")
    public void ShouldThrowExceptionWhenTryingCreateJsonNodeUsingAInvalidString(){
        String actualJsonString = "{\"nodo1\":\"valor1\",,}";

        Throwable expectedThrowable = assertThrows(IllegalArgumentException.class,()-> createJsonNodeFromString(actualJsonString));

        assertEquals(formatText("Cant create a Empty JsonNode. %s is a invalid String",actualJsonString),expectedThrowable.getMessage(),"The JsonNodes have differentes values");
    }

    @Test
    @DisplayName("Should validate valid Json")
    public void ShouldValidateValidJson(){
        String html =
                "<html>\n" +
                "  <head>\n" +
                        "  </head>\n" +
                "  <body>\n" +
                        "  </body>\n" +
                "</html>";
        assertFalse(isJSONValid(html), "Is a json valid");
    }

    @Test
    @DisplayName("Should get JsonNode from file")
    public void ShouldGetJsonNodeFromFile(){
        JsonNode jsonNode = getJsonNodeFromFile(new File("src/test/resources/__files/json/batman.json"));
        assertNotNull(jsonNode, "Can't get JsonNode");
    }

}
