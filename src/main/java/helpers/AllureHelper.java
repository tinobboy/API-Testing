package helpers;

import core.RequestSpecification;
import core.SoapRequestSpecification;
import io.restassured.response.Response;
import static listeners.StepReporterEventListener.*;
import static helpers.StringHelper.*;

public class AllureHelper {

    public static void attachRequestFile(RequestSpecification requestSpecification){
        String requestBaseUri = requestSpecification.getBaseUri();
        String pathString = requestSpecification.getFullPath();
        String urlComplete = formatText("%s%s", requestBaseUri, pathString);
        String body = buildBodyString(requestSpecification);
        attachFile("request", formatText("\nRequest: %s %s", urlComplete, body));
    }
    public static void attachRequestFile(SoapRequestSpecification soapRequestSpecification){
        String requestBaseUri = soapRequestSpecification.getBaseUri();
        String pathString = soapRequestSpecification.getPath();
        String urlComplete = formatText("%s%s", requestBaseUri, pathString);
        String body = buildBodyString(soapRequestSpecification);
        attachFile("request", formatText("\nRequest: %s %s", urlComplete, body));
    }
    public static void attachResponseFile(Response response){
        String responseString = buildResponseString(response);
        attachFile("response",responseString);
    }

    private static String buildResponseString(Response response) {
        return String.format("Response Time: %s\n\nHeaders: %s\nCookies: %s\nHTTP StatusCode: %s\nBody: %s\n",
                response.time(), response.getHeaders(), response.getCookies(), response.getStatusCode(), response.prettyPrint());
    }

    private static String buildBodyString(RequestSpecification requestSpecification) {
        return requestSpecification.bodyExists()
                ? formatText("\nBody: %s", requestSpecification.getBody().toPrettyString())
                : new String();
        }

    private static String buildBodyString(SoapRequestSpecification soapRequestSpecification) {
        return soapRequestSpecification.bodyExists()
                ? formatText("\nBody: %s", soapRequestSpecification.getBodyString())
                : new String();
    }

}
