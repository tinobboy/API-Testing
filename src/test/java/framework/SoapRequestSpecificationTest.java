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

import java.util.List;


public class SoapRequestSpecificationTest {
    SoapRequestSpecification soapRequestSpecification;
    NodeHelper nodeHelper;
    Node node;
    String path = "calculator.asmx";

    public SoapRequestSpecificationTest() {
        nodeHelper = new NodeHelper();
    }

    @Test
    @DisplayName("Execute SOAP request")
    public void executeRequestMadeByCode(){
        RequestSteps step = new RequestSteps();
        String filePath = "src/test/resources/xmldata/test/calculator.xml";
        String url = "http://www.dneonline.com";
        node = nodeHelper.getNodeFromFile(filePath);

        soapRequestSpecification = new SoapRequestSpecification(RequestMethods.POST, path,"SOAP request");
        soapRequestSpecification.setUrl(url);
        soapRequestSpecification.addHeader("Content-Type", "text/xml");
        soapRequestSpecification.setBody(node);
        SoapResponseSpecification soapResponseSpecification = step.executeRequest(soapRequestSpecification);
        Integer actualCode = soapResponseSpecification.getStatusCode();

        Assertions.assertEquals(200, actualCode, "Status code is not as expected");
        String addResult = soapResponseSpecification.getValueFromBody("AddResult");
        Assertions.assertEquals("5", addResult, "Incorrect result");
    }
    @Test
    @DisplayName("Modify the value of a XML node")
    public void modifyValue(){
        String filePath = "src/test/resources/xmldata/test/calculator.xml";
        String newValue = "8";
        node = nodeHelper.getNodeFromFile(filePath);

        soapRequestSpecification = new SoapRequestSpecification(RequestMethods.POST, path,"Test SOAP Request");
        soapRequestSpecification.setBody(node);
        soapRequestSpecification.setBody("intA", newValue);
        String intA = soapRequestSpecification.getValueFromBody("intA");

        Assertions.assertEquals(newValue, intA, "Values aren't equals");
    }
    @Test
    @DisplayName("Get node values from an array of XML")
    public void getArrayValues(){
        String filePath = "src/test/resources/xmldata/test/calculatorArray.xml";
        node = nodeHelper.getNodeFromFile(filePath);
        soapRequestSpecification = new SoapRequestSpecification(RequestMethods.POST, path,"Test SOAP Request");
        soapRequestSpecification.setBody(node);
        List<String> listaNodos = soapRequestSpecification.getListValuesFromBody("intA");

        Assertions.assertEquals("3", listaNodos.get(0),"Values aren't equals");
        Assertions.assertEquals("5", listaNodos.get(1),"Values aren't equals");
    }
}
