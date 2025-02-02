package framework;

import com.fasterxml.jackson.databind.JsonNode;
import constants.RequestContentType;
import constants.RequestHeaders;
import constants.RequestMethods;
import core.RequestSpecification;
import core.ResponseSpecification;
import helpers.ObjectMapperHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.RequestSteps;

import static helpers.ObjectMapperHelper.createJsonNodeFromString;
import static org.junit.jupiter.api.Assertions.*;

public class ResponseSpecificationTest {
    private ResponseSpecification responseSpecification;

    @Test
    @DisplayName("Test GET public service")
    public void sanityTest() {
        responseSpecification = executePublicService();

        assertEquals(200, responseSpecification.getStatusCode(), "Incorrect Status code");
    }

    @Test
    @DisplayName("Validate Content-Type")
    public void validateContentType() {
        responseSpecification = executePublicService();

        assertTrue(responseSpecification.contentTypeExists(), "ContentType doesn't exist");
        assertEquals(RequestContentType.JSON.getDescription(), responseSpecification.getContentType(), "Incorrect ContentType");
    }

    @Test
    @DisplayName("Get value from body response")
    public void getValueFromBody() {
        ResponseSpecification responseSpecification;
        responseSpecification = executePublicService();
        JsonNode jsonNode = createJsonNodeFromString("{\"campo1\":\"valor 1\"}");
        responseSpecification.setBody(jsonNode);

        assertEquals("valor 1", responseSpecification.getValueFromBody("campo1"), "Values are differents");
    }

    @Test
    @DisplayName("Validate JSON Body")
    public void validateJsonBody() {
        responseSpecification = executePublicService();

        assertTrue(ObjectMapperHelper.isJSONValid(responseSpecification.getBody().asText()), "Body isn't a valid JSON");
    }

    @Test
    @DisplayName("Validate field from body")
    public void validateFieldFromBody() {
        responseSpecification = executePublicService();
        assertEquals("Effect Monster", responseSpecification.getBody().at("/data").get(0).at("/type").asText(),"Values are not equals");
    }

    private ResponseSpecification executePublicService(){
        String url = "https://db.ygoprodeck.com";
        String path = "/api/v7/cardinfo.php";
        RequestSpecification requestSpecification = new RequestSpecification(url, RequestMethods.GET);
        requestSpecification.setPaths(path);
        requestSpecification.setHeaders(RequestHeaders.CONTENT_TYPE, RequestContentType.JSON.getDescription());
        requestSpecification.setParameters("name", "jinzo");

        return new RequestSteps().executeRequest(requestSpecification);
    }

}
