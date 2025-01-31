package steps.APISteps;

import constants.RequestMethods;
import core.PandoraRequest;
import core.PandoraResponse;
import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.restassured.http.ContentType;
import steps.BaseSteps;
import steps.RequestSteps;

public class RickAndMortySteps {

    private static BaseSteps step = new BaseSteps();
    private PandoraResponse respuestaActual;
    private PandoraRequest pandoraRequest;


    @Before("@test")
    public void beforeScenario(){
        pandoraRequest = new PandoraRequest("https://rickandmortyapi.com/api", RequestMethods.GET);
        pandoraRequest.setPaths("/character/2");
    }

    @Dado("^una url completa$")
    public void unaUrlCompleta() {

    }

    @Cuando("^se ejecuta el request$")
    public void seEjecutaElRequest() {
        respuestaActual = new RequestSteps().executeRequest(pandoraRequest);
    }

    @Entonces("^el resultado fue exitoso$")
    public void elResultadoFueExitoso() {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);

    }
}
