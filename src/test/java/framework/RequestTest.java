package framework;

import com.github.tomakehurst.wiremock.WireMockServer;
import constants.RequestContentType;
import constants.RequestHeaders;
import constants.RequestMethods;
import core.PandoraRequest;
import core.PandoraResponse;
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

        PandoraRequest pandoraRequest = new PandoraRequest("http://localhost", RequestMethods.GET);
        pandoraRequest.setPaths("/superheros");
        pandoraRequest.setHeaders(RequestHeaders.CONTENT_TYPE,RequestContentType.JSON.getDescription());

        PandoraResponse pandoraResponse = new RequestSteps().executeRequest(pandoraRequest);

        assertEquals(200,pandoraResponse.getStatusCode());

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

        PandoraRequest pandoraRequest = new PandoraRequest("http://localhost", RequestMethods.GET);
        pandoraRequest.setPaths("/superheros/search");
        pandoraRequest.setHeaders(RequestHeaders.CONTENT_TYPE, RequestContentType.JSON.getDescription());
        pandoraRequest.setParameters("publisher","Marvel");
        pandoraRequest.setParameters("id","1");

        PandoraResponse pandoraResponse = new RequestSteps().executeRequest(pandoraRequest);

        assertEquals(200,pandoraResponse.getStatusCode());
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

        PandoraRequest pandoraRequest = new PandoraRequest("http://localhost", RequestMethods.POST);
        pandoraRequest.setPaths("/superheros/add");
        pandoraRequest.setHeaders(RequestHeaders.CONTENT_TYPE,RequestContentType.JSON.getDescription());
        pandoraRequest.setBody("superhero","Dr. Strange");
        pandoraRequest.setBody("publisher","Marvel Comics");
        pandoraRequest.setBody("alter_ego","Stephen Strange");
        pandoraRequest.setBody("first_appeareance","Strange Tales #110");
        pandoraRequest.setBody("characters","Stephen Strange");

        PandoraResponse pandoraResponse = new RequestSteps().executeRequest(pandoraRequest);

        assertEquals(200,pandoraResponse.getStatusCode());
    }

    @AfterAll
    public static void tearDown(){
        wireMockServer.stop();
    }
}
