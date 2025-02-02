package core;

import helpers.NodeHelper;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SoapResponseSpecification {

    private String name;
    private HashMap<String, String> headers;
    private Node body;
    private NodeHelper nodeHelper;
    private Response response;

    public SoapResponseSpecification(Response response)  {
        this.response = response;
        this.nodeHelper = new NodeHelper();
        setHeaders();
        setBody();
    }

    public void setBody(){
        this.body = nodeHelper.stringToNode(response.getBody().asString());
    }

    public void setBody(Node node){ this.body = node; }

    public Integer getStatusCode() {
        return response.statusCode();
    }

    public boolean headersExists() { return this.headers != null; }

    public ContentType getContentType() {
     String  contentType =  "";
     if (getHeaders().containsKey("Content-Type")) {
         contentType = getHeaders().get("Content-Type");
         return ContentType.fromContentType(contentType);
     }
     if (getHeaders().containsKey("content-type")) {
         contentType = getHeaders().get("content-type");
         return ContentType.fromContentType(contentType);
     }
     throw new IllegalArgumentException("No se encontr\u00F3 el content type");
    }

    public boolean contentTypeExists(){
        return getHeaders().containsKey("Content-Type") || getHeaders().containsKey("content-type");
    }

    public void setHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        List<Header> headerList = response.getHeaders().asList();
        for (Header header : headerList){
            headers.put(header.getName(), header.getValue());
        }
        this.headers = headers;
    }

    public HashMap<String, String> getHeaders() { return this.headers; }

    public String getName() { return this.name; }

    public Node getBody() {
        if (this.body == null || "".equals(this.body)) {
            throw new RuntimeException("El body esperado est\u00E1 vac\u00EDo");
        }
        return this.body;
    }

    public boolean bodyExists() { return this.body != null; }

    public String getBodyString() {
        return nodeHelper.nodeToString(getBody());
    }

    /**
     * Metodo para obtener el valor de un campo o nodo del body de un request
     * @param
     */
    public String getValueFromBody(String node){
        Node nodo = nodeHelper.getNode(node, body);
        return nodo.getTextContent();
    }

    /**
     * Método para obtener los valores de un campo o nodo del array de un body de un request
     * @param
     */
    public List<String> getListValuesFromBody(String node){
        List<String> values = new ArrayList<>();
        NodeList nodeList = nodeHelper.getNodeList(node, body);
        for (int i=0; i<nodeList.getLength(); i++){
            values.add(nodeList.item(i).getTextContent());
        }
        return values;
    }

    /**
     * Método para obtener lista de nodos
     * @param node
     * @return
     */
    public NodeList getNodeListFromBody(String node){
        return nodeHelper.getNodeList(node, body);
    }

    @Override
    public String toString() {
        return "Response obtenido de: " + getName();
    }


}
