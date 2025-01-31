package framework;

import constants.RequestMethods;
import core.PandoraSoapRequest;
import core.PandoraSoapResponse;
import helpers.NodeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;
import steps.RequestSteps;


public class PandoraSoapResponseTest {
    PandoraSoapResponse pandoraSoapResponse;

    public PandoraSoapResponseTest() { }

    @Test
    @DisplayName("Get value from response")
    public void getValue(){
        ejecutarRequest();
        String addResult = pandoraSoapResponse.getValueFromBody("AddResult");
        Assertions.assertEquals("5", addResult, "Result is wrong");
    }

    @Test
    @DisplayName("Get StatusCode")
    public void getStatusCode(){
        ejecutarRequest();
        int statusCode = pandoraSoapResponse.getStatusCode();
        Assertions.assertEquals(200, statusCode, "StatusCode is wrong");
    }

    private void ejecutarRequest(){
        RequestSteps step = new RequestSteps();
        String filePath = "src/test/resources/xmldata/test/calculator.xml";
        Node node = new NodeHelper().getNodeFromFile(filePath);
        String url = "http://www.dneonline.com";
        String path = "calculator.asmx";

        PandoraSoapRequest pandoraSoapRequest = new PandoraSoapRequest(RequestMethods.POST, path,"SOAP request");
        pandoraSoapRequest.setUrl(url);
        pandoraSoapRequest.addHeader("Content-Type", "text/xml");
        pandoraSoapRequest.setBody(node);
        this.pandoraSoapResponse = step.executeRequest(pandoraSoapRequest);
    }
}
