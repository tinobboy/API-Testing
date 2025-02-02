package framework;

import com.github.tomakehurst.wiremock.WireMockServer;
import constants.RequestContentType;
import constants.RequestHeaders;
import constants.RequestMethods;
import core.RequestSpecification;
import core.ResponseSpecification;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import steps.RequestSteps;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RequestTest {

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setUp(){
        wireMockServer = new WireMockServer();
        configureFor("localhost", 8080);
        wireMockServer.start();
    }

    @Test
    @DisplayName("Should can send a GET request without query params")
    public void shouldCanSendAGetRequestWithoutQueryParams(){
        stubFor(get(urlEqualTo("/superheros"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile("/json/superheros.json")));

        RequestSpecification requestSpecification = new RequestSpecification("http://localhost", RequestMethods.GET);
        requestSpecification.setPaths("/superheros");
        requestSpecification.setHeaders(RequestHeaders.CONTENT_TYPE,RequestContentType.JSON.getDescription());

        ResponseSpecification responseSpecification = new RequestSteps().executeRequest(requestSpecification);

        assertEquals(200, responseSpecification.getStatusCode());

    }

    @Test
    @DisplayName("Should can send a GET request with query params")
    public void shouldCanSendAGetRequestWithQueryParams(){
        stubFor(get(urlEqualTo("/superheros/search?publisher=Marvel&id=1"))
                .withQueryParam("publisher",equalTo("Marvel"))
                .withQueryParam("id",equalTo("1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile("/json/batman.json")));

        RequestSpecification requestSpecification = new RequestSpecification("http://localhost", RequestMethods.GET);
        requestSpecification.setPaths("/superheros/search");
        requestSpecification.setHeaders(RequestHeaders.CONTENT_TYPE, RequestContentType.JSON.getDescription());
        requestSpecification.setParameters("publisher","Marvel");
        requestSpecification.setParameters("id","1");

        ResponseSpecification responseSpecification = new RequestSteps().executeRequest(requestSpecification);

        assertEquals(200, responseSpecification.getStatusCode());
    }

    @Test
    @DisplayName("Should can send a POST request with body")
    public void shouldCanSendAPOSTRequestWithBody(){
        stubFor(post(urlEqualTo("/superheros/add"))
                .withHeader("Content-Type",equalTo("application/json"))
                .withRequestBody(equalToJson("{\"superhero\" : \"Dr. Strange\",\"publisher\" : \"Marvel Comics\",\"alter_ego\" : \"Stephen Strange\",\"first_appeareance\" : \"Strange Tales #110\",\"characters\" : \"Stephen Strange\"}", true, true))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile("/json/post_ok.json")));

        RequestSpecification requestSpecification = new RequestSpecification("http://localhost", RequestMethods.POST);
        requestSpecification.setPaths("/superheros/add");
        requestSpecification.setHeaders(RequestHeaders.CONTENT_TYPE,RequestContentType.JSON.getDescription());
        requestSpecification.setBody("superhero","Dr. Strange");
        requestSpecification.setBody("publisher","Marvel Comics");
        requestSpecification.setBody("alter_ego","Stephen Strange");
        requestSpecification.setBody("first_appeareance","Strange Tales #110");
        requestSpecification.setBody("characters","Stephen Strange");

        ResponseSpecification responseSpecification = new RequestSteps().executeRequest(requestSpecification);

        assertEquals(200, responseSpecification.getStatusCode());
    }

    @AfterAll
    public static void tearDown(){
        wireMockServer.stop();
    }
}
