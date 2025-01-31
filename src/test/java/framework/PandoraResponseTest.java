package framework;

import com.fasterxml.jackson.databind.JsonNode;
import constants.RequestContentType;
import constants.RequestHeaders;
import constants.RequestMethods;
import core.PandoraRequest;
import core.PandoraResponse;
import helpers.ObjectMapperHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.RequestSteps;

import static helpers.ObjectMapperHelper.createJsonNodeFromString;
import static org.junit.jupiter.api.Assertions.*;

public class PandoraResponseTest {
    private PandoraResponse pandoraResponse;

    @Test
    @DisplayName("Test GET public service")
    public void sanityTest() {
        pandoraResponse = executePublicService();

        assertEquals(200,pandoraResponse.getStatusCode(), "Incorrect Status code");
    }

    @Test
    @DisplayName("Validate Content-Type")
    public void validateContentType() {
        pandoraResponse = executePublicService();

        assertTrue(pandoraResponse.contentTypeExists(), "ContentType doesn't exist");
        assertEquals(RequestContentType.JSON.getDescription(), pandoraResponse.getContentType(), "Incorrect ContentType");
    }

    @Test
    @DisplayName("Get value from body response")
    public void getValueFromBody() {
        PandoraResponse pandoraResponse;
        pandoraResponse = executePublicService();
        JsonNode jsonNode = createJsonNodeFromString("{\"campo1\":\"valor 1\"}");
        pandoraResponse.setBody(jsonNode);

        assertEquals("valor 1", pandoraResponse.getValueFromBody("campo1"), "Values are differents");
    }

    @Test
    @DisplayName("Validate JSON Body")
    public void validateJsonBody() {
        pandoraResponse = executePublicService();

        assertTrue(ObjectMapperHelper.isJSONValid(pandoraResponse.getBody().asText()), "Body isn't a valid JSON");
    }

    @Test
    @DisplayName("Validate field from body")
    public void validateFieldFromBody() {
        pandoraResponse = executePublicService();
        assertEquals("Effect Monster", pandoraResponse.getBody().at("/data").get(0).at("/type").asText(),"Values are not equals");
    }

    private PandoraResponse executePublicService(){
        String url = "https://db.ygoprodeck.com";
        String path = "/api/v7/cardinfo.php";
        PandoraRequest pandoraRequest = new PandoraRequest(url, RequestMethods.GET);
        pandoraRequest.setPaths(path);
        pandoraRequest.setHeaders(RequestHeaders.CONTENT_TYPE, RequestContentType.JSON.getDescription());
        pandoraRequest.setParameters("name", "jinzo");

        return new RequestSteps().executeRequest(pandoraRequest);
    }

}
