package helpers;

import core.PandoraRequest;
import core.PandoraSoapRequest;
import io.restassured.response.Response;
import static listeners.StepReporterEventListener.*;
import static helpers.StringHelper.*;

public class AllureHelper {

    public static void attachRequestFile(PandoraRequest pandoraRequest){
        String requestBaseUri = pandoraRequest.getBaseUri();
        String pathString = pandoraRequest.getFullPath();
        String urlComplete = formatText("%s%s", requestBaseUri, pathString);
        String body = buildBodyString(pandoraRequest);
        attachFile("request", formatText("\nRequest: %s %s", urlComplete, body));
    }
    public static void attachRequestFile(PandoraSoapRequest pandoraSoapRequest){
        String requestBaseUri = pandoraSoapRequest.getBaseUri();
        String pathString = pandoraSoapRequest.getPath();
        String urlComplete = formatText("%s%s", requestBaseUri, pathString);
        String body = buildBodyString(pandoraSoapRequest);
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

    private static String buildBodyString(PandoraRequest pandoraRequest) {
        return pandoraRequest.bodyExists()
                ? formatText("\nBody: %s",pandoraRequest.getBody().toPrettyString())
                : new String();
        }

    private static String buildBodyString(PandoraSoapRequest pandoraSoapRequest) {
        return pandoraSoapRequest.bodyExists()
                ? formatText("\nBody: %s",pandoraSoapRequest.getBodyString())
                : new String();
    }

}
