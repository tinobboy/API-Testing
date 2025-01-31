package steps;


import configuraciones.Proyecto;
import configuraciones.Servicio;
import constants.*;
import core.PandoraRequest;
import core.PandoraResponse;
import core.PandoraSoapResponse;
import helpers.ObjectMapperHelper;
import io.qameta.allure.Step;
import io.qameta.allure.model.Label;
import io.qameta.allure.util.ResultsUtils;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import java.util.Arrays;
import java.util.List;

import static io.qameta.allure.Allure.getLifecycle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseSteps {
    private static String token, tokenSoap;


    @Step("Se inicia una request del tipo {metodo}")
    public PandoraRequest buildPandoraRequest(Servicio servicio, Proyecto proyecto, RequestMethods metodo) {
        PandoraRequest pandoraRequest = new PandoraRequest(proyecto.getUrl(), metodo);
        pandoraRequest.setPaths(servicio.getPath());
        pandoraRequest.setHeaders(RequestHeaders.AUTHORIZATION, "Bearer " + token);
        return pandoraRequest;
    }

    @Step("Validar que el c\u00f3digo de respuesta sea: {expectedCode}")
    public void validateResponseCode(PandoraResponse pandoraResponse, int expectedCode) {
        int actualCode = pandoraResponse.getStatusCode();
        TipoError tipoError = TipoError.getTipoErrorSegunCodigos(actualCode, expectedCode);
        if (expectedCode != 503 && actualCode == 503 || expectedCode != 504 && actualCode == 504 || expectedCode != 500 && actualCode == 500) {
            addTestCaseLabel("servicio no disponible: " + actualCode);
        }
        assertEquals(expectedCode, actualCode, tipoError + "\nC\u00f3digo de respuesta inesperado.\nDescripcion: " + HTTPCodes.descriptionOf(actualCode) + "\n");
    }

    @Step("Validar que el c\u00f3digo de respuesta sea: {expectedCode}")
    public void validateResponseCode(PandoraSoapResponse pandoraSoapResponse, int expectedCode) {
        int actualCode = pandoraSoapResponse.getStatusCode();
        TipoError tipoError = TipoError.getTipoErrorSegunCodigos(actualCode, expectedCode);
        if (expectedCode != 503 && actualCode == 503 || expectedCode != 504 && actualCode == 504 || expectedCode != 500 && actualCode == 500) {
            addTestCaseLabel("servicio no disponible: " + actualCode);
        }
        assertEquals(expectedCode, actualCode, tipoError + "\nC\u00f3digo de respuesta inesperado.\nDescripcion: " + HTTPCodes.descriptionOf(actualCode) + "\n");
    }

    @Step("Valida Code y Content Type")
    public void validateResponseCodeyContentType(PandoraResponse pandoraResponse, int expectedCode, ContentType expectedContentType) {
        validateResponseCode(pandoraResponse, expectedCode);
        if (pandoraResponse.getContentType() != null) {
            validateResponseType(pandoraResponse, expectedContentType);
            if (pandoraResponse.getBody() != null)
                Assertions.assertFalse(pandoraResponse.getBody().isEmpty(), "El body actual se encuentra vacio");
            else
                Assertions.assertFalse(pandoraResponse.getBodyString().isEmpty(), "El body actual se encuentra vacio");
        }
    }

    private void addTestCaseLabel(String tagLabel) {
        getLifecycle().updateTestCase(testResult -> {
            List<Label> labels = testResult.getLabels();
            labels.add(ResultsUtils.createTagLabel(tagLabel));
            testResult.setLabels(labels);
        });
    }

    /**
     * Valida que el content type sean iguales.
     * Ej. application/json
     * En caso de no coincidir, se considera ERROR CRITICO si el código de respuesta esperado es 200.
     * De lo contrario se considera ERROR TRIVIAL.
     */
    @Step("Validar que la respuesta sea del tipo: {contentType} para casos Gherkins")
    public void validateResponseType(PandoraResponse pandoraResponse, ContentType contentType) {
        switch ((contentType != null) ? contentType : ContentType.ANY) {
            case JSON:
                validateContentType(contentType, "application/json", pandoraResponse);
                break;
            case TEXT:
                validateContentType(contentType, "text/plain", pandoraResponse);
                break;
            case XML:
                validateContentType(contentType, "application/xml", pandoraResponse);
                break;
            case HTML:
                validateContentType(contentType, "text/html", pandoraResponse);
                break;
            case ANY:
                String contentTypePdf = String.valueOf(pandoraResponse.getContentType());
                Assertions.assertEquals("application/pdf", contentTypePdf, TipoError.CRITICO + "\nSe esperaba un Content Type application/pdf y se recibio un Content Type " + contentTypePdf);
                break;
            default:
                throw new IllegalArgumentException("No existe funcionalidad para validar " + contentType);
        }
    }

    /**
     * Método que recibe un objeto de la enumeración {@link ContentType}, un string con el tipo que se espera encontrar, y la respuesta actual del servicio,
     * y evalúa si el content type de la respuesta coincide con lo que se espera.
     *
     * @param contentType
     * @param expectedContentType
     * @param respuestaActual
     */
    private void validateContentType(ContentType contentType, String expectedContentType, PandoraResponse respuestaActual) {
        TipoError tipoError = TipoError.TRIVIAL;
        String contentTypeActual = String.valueOf(respuestaActual.getContentType());
        String contentTypeExpected = Arrays.stream(contentType.getContentTypeStrings())
                .filter(value -> value.equals(expectedContentType))
                .findFirst().get();
        if (respuestaActual.getStatusCode() == 200) tipoError = TipoError.CRITICO;
        assertTrue(contentTypeActual.contains(contentTypeExpected),
                tipoError + "\nSe esperaba un Content Type " + expectedContentType + " y se recibio un Content Type " + contentTypeActual);
        if(contentType.equals(ContentType.JSON))
        assertTrue(ObjectMapperHelper.isJSONValid(respuestaActual.getBody().asText()),
                tipoError + "\nEl Body de respuesta no tiene formato Json.\nLa respuesta es:\n" + respuestaActual.getBody().asText());

    }


}
