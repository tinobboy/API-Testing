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

import java.util.List;


public class PandoraSoapRequestTest {
    PandoraSoapRequest pandoraSoapRequest;
    NodeHelper nodeHelper;
    Node node;
    String path = "calculator.asmx";

    public PandoraSoapRequestTest() {
        nodeHelper = new NodeHelper();
    }

    @Test
    @DisplayName("Execute SOAP request")
    public void executeRequestMadeByCode(){
        RequestSteps step = new RequestSteps();
        String filePath = "src/test/resources/xmldata/test/calculator.xml";
        String url = "http://www.dneonline.com";
        node = nodeHelper.getNodeFromFile(filePath);

        pandoraSoapRequest = new PandoraSoapRequest(RequestMethods.POST, path,"SOAP request");
        pandoraSoapRequest.setUrl(url);
        pandoraSoapRequest.addHeader("Content-Type", "text/xml");
        pandoraSoapRequest.setBody(node);
        PandoraSoapResponse pandoraSoapResponse = step.executeRequest(pandoraSoapRequest);
        Integer actualCode = pandoraSoapResponse.getStatusCode();

        Assertions.assertEquals(200, actualCode, "Status code is not as expected");
        String addResult = pandoraSoapResponse.getValueFromBody("AddResult");
        Assertions.assertEquals("5", addResult, "Incorrect result");
    }
    @Test
    @DisplayName("Modify the value of a XML node")
    public void modifyValue(){
        String filePath = "src/test/resources/xmldata/test/calculator.xml";
        String newValue = "8";
        node = nodeHelper.getNodeFromFile(filePath);

        pandoraSoapRequest = new PandoraSoapRequest(RequestMethods.POST, path,"Test SOAP Request");
        pandoraSoapRequest.setBody(node);
        pandoraSoapRequest.setBody("intA", newValue);
        String intA = pandoraSoapRequest.getValueFromBody("intA");

        Assertions.assertEquals(newValue, intA, "Values aren't equals");
    }
    @Test
    @DisplayName("Get node values from an array of XML")
    public void getArrayValues(){
        String filePath = "src/test/resources/xmldata/test/calculatorArray.xml";
        node = nodeHelper.getNodeFromFile(filePath);
        pandoraSoapRequest = new PandoraSoapRequest(RequestMethods.POST, path,"Test SOAP Request");
        pandoraSoapRequest.setBody(node);
        List<String> listaNodos = pandoraSoapRequest.getListValuesFromBody("intA");

        Assertions.assertEquals("3", listaNodos.get(0),"Values aren't equals");
        Assertions.assertEquals("5", listaNodos.get(1),"Values aren't equals");
    }
}
