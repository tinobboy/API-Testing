package framework;

import constants.RequestMethods;
import core.SoapRequestSpecification;
import core.SoapResponseSpecification;
import helpers.NodeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;
import steps.RequestSteps;


public class SoapResponseSpecificationTest {
    SoapResponseSpecification soapResponseSpecification;

    public SoapResponseSpecificationTest() { }

    @Test
    @DisplayName("Get value from response")
    public void getValue(){
        ejecutarRequest();
        String addResult = soapResponseSpecification.getValueFromBody("AddResult");
        Assertions.assertEquals("5", addResult, "Result is wrong");
    }

    @Test
    @DisplayName("Get StatusCode")
    public void getStatusCode(){
        ejecutarRequest();
        int statusCode = soapResponseSpecification.getStatusCode();
        Assertions.assertEquals(200, statusCode, "StatusCode is wrong");
    }

    private void ejecutarRequest(){
        RequestSteps step = new RequestSteps();
        String filePath = "src/test/resources/xmldata/test/calculator.xml";
        Node node = new NodeHelper().getNodeFromFile(filePath);
        String url = "http://www.dneonline.com";
        String path = "calculator.asmx";

        SoapRequestSpecification soapRequestSpecification = new SoapRequestSpecification(RequestMethods.POST, path,"SOAP request");
        soapRequestSpecification.setUrl(url);
        soapRequestSpecification.addHeader("Content-Type", "text/xml");
        soapRequestSpecification.setBody(node);
        this.soapResponseSpecification = step.executeRequest(soapRequestSpecification);
    }
}
