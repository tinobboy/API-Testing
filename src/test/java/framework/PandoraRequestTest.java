package framework;

import com.fasterxml.jackson.databind.JsonNode;
import constants.RequestHeaders;
import constants.RequestMethods;
import core.PandoraRequest;

import static org.junit.jupiter.api.Assertions.*;
import static helpers.StringHelper.*;
import static helpers.ObjectMapperHelper.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static constants.RequestHeaders.AUTHORIZATION;
import static constants.RequestHeaders.KEEP_ALIVE;
import static java.lang.String.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class PandoraRequestTest {

    private final String defaultBaseUrl = "https://server.com.ar";
    private final RequestMethods defaultMethod = RequestMethods.GET;
    private PandoraRequest pandoraRequest;

    @BeforeEach
    public void setUp(){
        pandoraRequest = new PandoraRequest(defaultBaseUrl,defaultMethod);
    }

    @Test
    @DisplayName("Should modify all the headers")
    public void shouldModifyAllTheHeaders() {
        HashMap<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(AUTHORIZATION.getDescription(), "123456");
        expectedHeaders.put(KEEP_ALIVE.getDescription(), "true");

        HashMap<RequestHeaders, String> actualHeaders = new HashMap<>();
        actualHeaders.put(AUTHORIZATION, "123456");
        actualHeaders.put(KEEP_ALIVE, "true");

        assertNull(pandoraRequest.getHeaders(), "Request Headers are not null");
        pandoraRequest.setHeaders(actualHeaders);
        assertNotNull(pandoraRequest.getHeaders(), "Request Headers is still null. The Request Headers could not be set");
        assertEquals(expectedHeaders, pandoraRequest.getHeaders(), "Request Headers were set wrong");
    }

    @Test
    @DisplayName("Should modify only one header")
    public void shouldModifyOnlyOneHeader() {
        HashMap<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(AUTHORIZATION.getDescription(), "123456");
        expectedHeaders.put(KEEP_ALIVE.getDescription(), "false");

        HashMap<RequestHeaders, String> actualHeaders = new HashMap<>();
        actualHeaders.put(AUTHORIZATION, "123456");
        actualHeaders.put(KEEP_ALIVE, "true");
        pandoraRequest.setHeaders(actualHeaders);

        pandoraRequest.setHeaders(KEEP_ALIVE, "false");

        assertEquals(expectedHeaders, pandoraRequest.getHeaders(), "Request Headers was set wrong");
    }

    @Test
    @DisplayName("Should remove only one header")
    public void shouldRemoveOnlyOneHeader() {
        HashMap<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(AUTHORIZATION.getDescription(), "123456");

        HashMap<RequestHeaders, String> actualHeaders = new HashMap<>();
        actualHeaders.put(AUTHORIZATION, "123456");
        actualHeaders.put(KEEP_ALIVE, "true");
        pandoraRequest.setHeaders(actualHeaders);

        pandoraRequest.removeHeader(KEEP_ALIVE);

        assertEquals(expectedHeaders, pandoraRequest.getHeaders(), "Request Headers was not removed");
    }

    @Test
    @DisplayName("Should remove all the headers")
    public void shouldRemoveAllTheHeaders() {
        HashMap<RequestHeaders, String> actualHeaders = new HashMap<>();
        actualHeaders.put(AUTHORIZATION, "123456");
        actualHeaders.put(KEEP_ALIVE, "true");
        pandoraRequest.setHeaders(actualHeaders);

        pandoraRequest.removeAllHeaders();

        assertNull(pandoraRequest.getHeaders(), "Request Headers were not removed");
    }

    @Test
    @DisplayName("Should modify all the query parameters")
    public void shouldModifyAllTheQueryParameters() {
        HashMap<String, List<String>> expectedQueryParameters = new HashMap<>();
        List<String> expectedValuesOfParameterOne = List.of("1", "2", "3");
        List<String> expectedValuesOfParameterTwo = List.of("true");
        expectedQueryParameters.put("parameterOne", expectedValuesOfParameterOne);
        expectedQueryParameters.put("parameterTwo", expectedValuesOfParameterTwo);

        HashMap<String, List<String>> actualQueryParameters = new HashMap<>();
        List<String> actualValuesOfParameterOne = List.of("1", "2", "3");
        List<String> actualValuesOfParameterTwo = List.of("true");
        actualQueryParameters.put("parameterOne", actualValuesOfParameterOne);
        actualQueryParameters.put("parameterTwo", actualValuesOfParameterTwo);

        assertNull(pandoraRequest.getParameters(), "Query Parameters are not null");
        pandoraRequest.setParameters(actualQueryParameters);
        assertNotNull(pandoraRequest.getParameters(), "Query Parameters is still null. The Request Headers could not be set");
        assertEquals(expectedQueryParameters, pandoraRequest.getParameters(), "Query Parameters were set wrong");
    }

    @Test
    @DisplayName("Should modify only one query parameter")
    public void shouldModifyOnlyOneParameter() {
        HashMap<String, List<String>> expectedQueryParameters = new HashMap<>();
        List<String> expectedValuesOfParameterOne = List.of("4","5","6");
        List<String> expectedValuesOfParameterTwo = List.of("true");
        expectedQueryParameters.put("parameterOne", expectedValuesOfParameterOne);
        expectedQueryParameters.put("parameterTwo", expectedValuesOfParameterTwo);

        HashMap<String, List<String>> actualQueryParameters = new HashMap<>();
        List<String> actualValuesOfParameterOne = List.of("1", "2", "3");
        List<String> actualValuesOfParameterTwo = List.of("true");
        actualQueryParameters.put("parameterOne", actualValuesOfParameterOne);
        actualQueryParameters.put("parameterTwo", actualValuesOfParameterTwo);
        pandoraRequest.setParameters(actualQueryParameters);

        pandoraRequest.setParameters("parameterOne",List.of("4","5","6"));

        assertEquals(expectedQueryParameters, pandoraRequest.getParameters(), "Query Parameters was set wrong");
    }

    @Test
    @DisplayName("Should modify only one query parameter without List")
    public void shouldModifyOnlyOneParameterWithoutList() {
        HashMap<String, List<String>> expectedQueryParameters = new HashMap<>();
        List<String> expectedValuesOfParameterOne = List.of("5");
        List<String> expectedValuesOfParameterTwo = List.of("true");
        expectedQueryParameters.put("parameterOne", expectedValuesOfParameterOne);
        expectedQueryParameters.put("parameterTwo", expectedValuesOfParameterTwo);

        HashMap<String, List<String>> actualQueryParameters = new HashMap<>();
        List<String> actualValuesOfParameterOne = List.of("1", "2", "3");
        List<String> actualValuesOfParameterTwo = List.of("true");
        actualQueryParameters.put("parameterOne", actualValuesOfParameterOne);
        actualQueryParameters.put("parameterTwo", actualValuesOfParameterTwo);
        pandoraRequest.setParameters(actualQueryParameters);

        pandoraRequest.setParameters("parameterOne","5");

        assertEquals(expectedQueryParameters, pandoraRequest.getParameters(), "Query Parameters was not removed");
    }

    @Test
    @DisplayName("Should remove only one query parameter")
    public void shouldRemoveOnlyOneQueryParameter() {
        HashMap<String, List<String>> expectedQueryParameters = new HashMap<>();
        List<String> expectedValuesOfParameterOne = List.of("1","2","3");
        expectedQueryParameters.put("parameterOne", expectedValuesOfParameterOne);

        HashMap<String, List<String>> actualQueryParameters = new HashMap<>();
        List<String> actualValuesOfParameterOne = List.of("1", "2", "3");
        List<String> actualValuesOfParameterTwo = List.of("true");
        actualQueryParameters.put("parameterOne", actualValuesOfParameterOne);
        actualQueryParameters.put("parameterTwo", actualValuesOfParameterTwo);
        pandoraRequest.setParameters(actualQueryParameters);

        pandoraRequest.removeParameter("parameterTwo");

        assertEquals(expectedQueryParameters, pandoraRequest.getParameters(), "Query Parameters were set wrong");
    }

    @Test
    @DisplayName("Should remove all the query parameters")
    public void shouldRemoveAllTheQueryParameters() {
        HashMap<String, List<String>> actualQueryParameters = new HashMap<>();
        List<String> actualValuesOfParameterOne = List.of("1", "2", "3");
        List<String> actualValuesOfParameterTwo = List.of("true");
        actualQueryParameters.put("parameterOne", actualValuesOfParameterOne);
        actualQueryParameters.put("parameterTwo", actualValuesOfParameterTwo);
        pandoraRequest.setParameters(actualQueryParameters);

        pandoraRequest.removeAllParameters();

        assertNull(pandoraRequest.getParameters(), "Query Parameters was not removed");
    }

    @Test
    @DisplayName("Should modify all the paths")
    public void shouldModifyAllThePaths() {
        List<String> expectedPaths = List.of("path1","path2","path3");
        List<String> actualPaths = List.of("path1","path2","path3");

        assertNull(pandoraRequest.getPaths(), "Paths are not null");
        pandoraRequest.setPaths(actualPaths);
        assertNotNull(pandoraRequest.getPaths(), "Paths is still null. The Paths could not be set");

        assertEquals(expectedPaths, pandoraRequest.getPaths(), "Paths were set wrong");
    }

    @Test
    @DisplayName("Should get full path")
    public void shouldGetFullPath() {
        String expectedFullPaths = "/path1/path2/path3";

        String actuaFullPaths = "/path1/path2/path3";
        pandoraRequest.setPaths(actuaFullPaths);
        assertEquals(expectedFullPaths, pandoraRequest.getFullPath(), "Full Paths are not equals if you set them using String");

        List<String> actualPaths = List.of("path1","path2","path3");
        pandoraRequest.setPaths(actualPaths);
        assertEquals(expectedFullPaths, pandoraRequest.getFullPath(), "Full Paths are not equals if you set them using List");
    }

    @Test
    @DisplayName("Should get full path with parameters")
    public void shouldGetFullPathWithParameters() {
        String expectedFullPaths = "/path1/path2/path3?param1=1&param2=2";

        String actuaFullPaths = "/path1/path2/path3";
        pandoraRequest.setPaths(actuaFullPaths);
        pandoraRequest.setParameters("param1","1");
        pandoraRequest.setParameters("param2","2");
        assertEquals(expectedFullPaths, pandoraRequest.getFullPath(), "Full Paths are not equals if you set them using String");
    }

    @Test
    @DisplayName("Should remove all the paths")
    public void shouldRemoveAllThePaths() {
        String actuaFullPaths = "/path1/path2/path3";
        pandoraRequest.setPaths(actuaFullPaths);

        pandoraRequest.removeAllPaths();

        assertNull(pandoraRequest.getPaths(), "Paths were not removed");
    }

    @Test
    @DisplayName("Should modify protocol and host using URL")
    public void shouldModifyProtocolAndHostUsingURL() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String expectedProtocol = "https";
        String expectedHost = "server.com.ar";

        Method methodSetBaseUri = Class.forName("core.PandoraRequest").getDeclaredMethod("setBaseUri",String.class);
        methodSetBaseUri.setAccessible(true);
        methodSetBaseUri.invoke(pandoraRequest,defaultBaseUrl);

        assertEquals(expectedProtocol,pandoraRequest.getProtocol(),"Protocol was set wrong");
        assertEquals(expectedHost,pandoraRequest.getHost(),"Host was set wrong");
        assertEquals(defaultBaseUrl,pandoraRequest.getBaseUri(),"URL was set wrong");
    }

    @Test
    @DisplayName("Should throws exception when trying set a invalid Path")
    public void shouldThrowsExpcetionWhenTryingSetAInvalidPath()  {
        String actuaFullPaths = "path1/path2/path3";

        Throwable actualThrowable = assertThrows(IllegalArgumentException.class, ()-> pandoraRequest.setPaths(actuaFullPaths),"Expected Exception was not thrown");

        assertEquals(formatText("%s is not a valid Path. The path must start with '/' and have at least 2 characters", actuaFullPaths),actualThrowable.getMessage(),"The message of the Exception is not the expected");
    }

    @Test
    @DisplayName("Should throws exception when trying set a invalid URL")
    public void shouldThrowsExpcetionWhenTryingSetAInvalidUrl() throws ClassNotFoundException, NoSuchMethodException {
        String actualURL = "https:/server.com.ar";

        Method methodSetBaseUri = Class.forName("core.PandoraRequest").getDeclaredMethod("setBaseUri",String.class);
        methodSetBaseUri.setAccessible(true);

        Throwable actualThrowable = assertThrows(InvocationTargetException.class, ()-> methodSetBaseUri.invoke(pandoraRequest,actualURL),"Expected Exception was not thrown");

        assertEquals(formatText("%s is not a valid URL", actualURL),actualThrowable.getCause().getMessage(),"The message of the Exception is not the expected");
    }

    @Test
    @DisplayName("Should get the value of specific node from Body")
    public void shouldGetTheValueOfSpecificNodeFromBody() {
        String jsonString = "{\"campo1\":\"valor1\"}";
        JsonNode jsonNode = createJsonNodeFromString(jsonString);
        String expectedValue = "valor1";

        pandoraRequest.setBody(jsonNode);

        String actualValue = pandoraRequest.getValueFromBody("campo1");
        assertEquals(expectedValue,actualValue,"Values are differents");
    }

    @Test
    @DisplayName("Should remove specific node from Body")
    public void shouldRemoveSpecificNodeFromBody() {
        String jsonString = "{\"campo1\":\"valor1\",\"campo2\":\"valor\"}";
        JsonNode jsonNode = createJsonNodeFromString(jsonString);
        String expectedJsonString = "{\"campo1\":\"valor1\"}";

        pandoraRequest.setBody(jsonNode);
        pandoraRequest.removeNodeFromBody("campo2");

        String actualJsonString = valueOf(pandoraRequest.getBody());

        assertEquals(expectedJsonString,actualJsonString,"JsonNode are differents");
    }

    @Test
    @DisplayName("Should get body like a valid json string")
    public void shouldGetBodyLikeAValidJsonString(){
        String expectedJsonString = "{\"nodo1\":\"valor1\"}";
        JsonNode jsonNode = createJsonNodeFromString(expectedJsonString);
        pandoraRequest.setBody(jsonNode);

        String actualJsonString = pandoraRequest.getBodyLikeString();

        Assertions.assertEquals(expectedJsonString,actualJsonString,"Json Strings are differents");
    }

    @Test
    @DisplayName("Should set body node by node")
    public void shouldSetBodyNodeByNode(){
        String nodeName1 = "campo1";
        String nodeValue1 = "valor1";
        String nodeName2 = "campo2";
        String nodeValue2 = "valor2";
        String expectedJsonString = "{\"campo1\":\"valor1\",\"campo2\":\"valor2\"}";

        pandoraRequest.setBody(nodeName1,nodeValue1);
        pandoraRequest.setBody(nodeName2,nodeValue2);

        String actualJsonString = pandoraRequest.getBodyLikeString();

        Assertions.assertEquals(expectedJsonString,actualJsonString,"Json Strings are differents");
    }

    @Test
    @DisplayName("Should replace node in body if the node just already exist")
    public void shouldReplaceNodeInBodyIfTheNodeJustAlreadyExits(){
        String nodeName = "campo1";
        String nodeValue1 = "valor1";
        String nodeValue2 = "valor2";
        String expectedJsonString = "{\"campo1\":\"valor2\"}";

        pandoraRequest.setBody(nodeName,nodeValue1);
        pandoraRequest.setBody(nodeName,nodeValue2);

        String actualJsonString = pandoraRequest.getBodyLikeString();

        Assertions.assertEquals(expectedJsonString,actualJsonString,"Json Strings are differents");
    }

    @Test
    @DisplayName("Should remove all the body")
    public void shouldRemoveAllTheBody(){
        String jsonString = "{\"campo1\":\"valor1\",\"campo2\":\"valor2\"}";
        JsonNode jsonNode = createJsonNodeFromString(jsonString);

        pandoraRequest.setBody(jsonNode);
        pandoraRequest.removeAllBody();

        Assertions.assertNull(pandoraRequest.getBody(),"Body was not removed");
    }

}




