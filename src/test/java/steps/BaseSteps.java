package steps;


import configuraciones.Proyecto;
import configuraciones.Servicio;
import constants.*;
import core.RequestSpecification;
import core.ResponseSpecification;
import core.SoapResponseSpecification;
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
    public RequestSpecification buildPandoraRequest(Servicio servicio, Proyecto proyecto, RequestMethods metodo) {
        RequestSpecification requestSpecification = new RequestSpecification(proyecto.getUrl(), metodo);
        requestSpecification.setPaths(servicio.getPath());
        requestSpecification.setHeaders(RequestHeaders.AUTHORIZATION, "Bearer " + token);
        return requestSpecification;
    }

    @Step("Validar que el c\u00f3digo de respuesta sea: {expectedCode}")
    public void validateResponseCode(ResponseSpecification responseSpecification, int expectedCode) {
        int actualCode = responseSpecification.getStatusCode();
        TipoError tipoError = TipoError.getTipoErrorSegunCodigos(actualCode, expectedCode);
        if (expectedCode != 503 && actualCode == 503 || expectedCode != 504 && actualCode == 504 || expectedCode != 500 && actualCode == 500) {
            addTestCaseLabel("servicio no disponible: " + actualCode);
        }
        assertEquals(expectedCode, actualCode, tipoError + "\nC\u00f3digo de respuesta inesperado.\nDescripcion: " + HTTPCodes.descriptionOf(actualCode) + "\n");
    }

    @Step("Validar que el c\u00f3digo de respuesta sea: {expectedCode}")
    public void validateResponseCode(SoapResponseSpecification soapResponseSpecification, int expectedCode) {
        int actualCode = soapResponseSpecification.getStatusCode();
        TipoError tipoError = TipoError.getTipoErrorSegunCodigos(actualCode, expectedCode);
        if (expectedCode != 503 && actualCode == 503 || expectedCode != 504 && actualCode == 504 || expectedCode != 500 && actualCode == 500) {
            addTestCaseLabel("servicio no disponible: " + actualCode);
        }
        assertEquals(expectedCode, actualCode, tipoError + "\nC\u00f3digo de respuesta inesperado.\nDescripcion: " + HTTPCodes.descriptionOf(actualCode) + "\n");
    }

    @Step("Valida Code y Content Type")
    public void validateResponseCodeyContentType(ResponseSpecification responseSpecification, int expectedCode, ContentType expectedContentType) {
        validateResponseCode(responseSpecification, expectedCode);
        if (responseSpecification.getContentType() != null) {
            validateResponseType(responseSpecification, expectedContentType);
            if (responseSpecification.getBody() != null)
                Assertions.assertFalse(responseSpecification.getBody().isEmpty(), "El body actual se encuentra vacio");
            else
                Assertions.assertFalse(responseSpecification.getBodyString().isEmpty(), "El body actual se encuentra vacio");
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
    public void validateResponseType(ResponseSpecification responseSpecification, ContentType contentType) {
        switch ((contentType != null) ? contentType : ContentType.ANY) {
            case JSON:
                validateContentType(contentType, "application/json", responseSpecification);
                break;
            case TEXT:
                validateContentType(contentType, "text/plain", responseSpecification);
                break;
            case XML:
                validateContentType(contentType, "application/xml", responseSpecification);
                break;
            case HTML:
                validateContentType(contentType, "text/html", responseSpecification);
                break;
            case ANY:
                String contentTypePdf = String.valueOf(responseSpecification.getContentType());
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
    private void validateContentType(ContentType contentType, String expectedContentType, ResponseSpecification respuestaActual) {
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
