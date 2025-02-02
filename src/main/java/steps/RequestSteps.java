package steps;

import constants.RequestMethods;
import core.RequestSpecification;
import core.ResponseSpecification;
import core.SoapRequestSpecification;
import core.SoapResponseSpecification;
import helpers.AllureHelper;
import helpers.LoggerHelper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import static constants.RequestMethods.*;
import static io.restassured.RestAssured.given;
import static helpers.AllureHelper.*;
import static helpers.LoggerHelper.*;
import static helpers.StringHelper.*;


public class RequestSteps {

    private static LoggerHelper logger = new LoggerHelper(RequestSteps.class);

    @Step("Execute request")
    public ResponseSpecification executeRequest(RequestSpecification pandoraRequest) {
        io.restassured.specification.RequestSpecification requestSpecification = generateRequest(pandoraRequest);
        attachRequestFile(pandoraRequest);
        Response response = sendRequest(pandoraRequest.getMethod(), requestSpecification);
        attachResponseFile(response);
        return new ResponseSpecification(response);
    }

    @Step("Generar Request")
    protected io.restassured.specification.RequestSpecification generateRequest(RequestSpecification pandoraRequest) {

        RestAssuredConfig restAssuredConfig =  RestAssured
                .config()
                .httpClient(HttpClientConfig.httpClientConfig());

        io.restassured.specification.RequestSpecification requestSpecification =
                 given()
                .config(restAssuredConfig)
                .log().all()
                .relaxedHTTPSValidation("TLSv1.2")
                .baseUri(pandoraRequest.getBaseUri())
                .contentType(ContentType.JSON)
                .basePath(pandoraRequest.getFullPath());

        if (pandoraRequest.pathExists()) {
            HashMap<String,String> restAssuredHeaders = new HashMap<>();
            //pandoraRequest.getHeaders().forEach(restAssuredHeaders::put);
            requestSpecification = requestSpecification
                    .headers(restAssuredHeaders);
        }
        if (pandoraRequest.parametersExists()) {
            requestSpecification = requestSpecification
                    .queryParams(pandoraRequest.getParameters());
        }
        if (pandoraRequest.bodyExists() && Arrays.asList(new RequestMethods[]{POST, PUT, PATCH}).contains(pandoraRequest.getMethod())) {
            requestSpecification = requestSpecification
                    .body(pandoraRequest.getBodyLikeString());
        }

        return requestSpecification;
    }

    @Step("Execute Request SOAP")
    public SoapResponseSpecification executeRequest(SoapRequestSpecification soapRequestSpecification) {
        io.restassured.specification.RequestSpecification requestSpecification = this.generateRequest(soapRequestSpecification);
        AllureHelper.attachRequestFile(soapRequestSpecification);
        Response response = this.sendRequest(soapRequestSpecification.getMethod(), requestSpecification);
        AllureHelper.attachResponseFile(response);
        return new SoapResponseSpecification(response);
    }

    @Step("Generar Request SOAP")
    protected io.restassured.specification.RequestSpecification generateRequest(SoapRequestSpecification request) {

        RestAssuredConfig restAssuredConfig =  RestAssured.config().httpClient(HttpClientConfig.httpClientConfig()
                .setParam("http.socket.timeout",150000)
                .setParam("http.connection.timeout",150000));

        io.restassured.specification.RequestSpecification requestSpecification = given()
                .config(restAssuredConfig)
                .log().all()
                .relaxedHTTPSValidation("TLSv1.2")
                .baseUri(request.getBaseUri())
                .contentType(ContentType.XML)
                .basePath(request.getPath());

        if (request.headersExists()) {
            requestSpecification = requestSpecification
                    .headers(request.getHeaders());
        }
        if (request.bodyExists() && request.getMethod().equals(POST)) {
            requestSpecification = requestSpecification
                    .body(request.getBodyString());
        }

        return requestSpecification;
    }

    @Step("Send Request")
    protected Response sendRequest(RequestMethods method, io.restassured.specification.RequestSpecification requestSpecification) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        printInfoMessage(formatText("Sending request at %s",dateFormat.format(new Date())));
        Response response = requestSpecification.when().urlEncodingEnabled(false).request(method.getType());
        return response;
    }


}
