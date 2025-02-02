package steps.APISteps;

import constants.RequestMethods;
import core.RequestSpecification;
import core.ResponseSpecification;
import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.restassured.http.ContentType;
import steps.BaseSteps;
import steps.RequestSteps;

public class RickAndMortySteps {

    private static BaseSteps step = new BaseSteps();
    private ResponseSpecification respuestaActual;
    private RequestSpecification requestSpecification;


    @Before("@test")
    public void beforeScenario(){
        requestSpecification = new RequestSpecification("https://rickandmortyapi.com/api", RequestMethods.GET);
        requestSpecification.setPaths("/character/2");
    }

    @Dado("^una url completa$")
    public void unaUrlCompleta() {

    }

    @Cuando("^se ejecuta el request$")
    public void seEjecutaElRequest() {
        respuestaActual = new RequestSteps().executeRequest(requestSpecification);
    }

    @Entonces("^el resultado fue exitoso$")
    public void elResultadoFueExitoso() {
        step.validateResponseCodeyContentType(respuestaActual, 200, ContentType.JSON);

    }
}
