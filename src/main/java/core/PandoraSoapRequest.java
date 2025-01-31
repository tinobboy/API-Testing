package core;

import constants.RequestMethods;
import helpers.NodeHelper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PandoraSoapRequest {

    private String name;
    private HashMap<String, String> headers;
    private String host;
    private String protocol;
    private Node body;
    private NodeHelper nodeHelper;
    private List<String> path;
    private RequestMethods method;
    private String token;

    /**
     * Contructor de Requespecification a partir de un verbo HTTP, un path y un nombre.
     * @param method Objecto que representa el tipo de metodo del request.
     * @param paths String con formato url valido.
     * @param name  Nombre para identificar el objeto.
     * <hr>
     * @see RequestMethods
     */
    public PandoraSoapRequest(RequestMethods method, String paths, String name){
        this.name = name;
        this.method = method;
        this.nodeHelper = new NodeHelper();
        this.path = new ArrayList<String>(Arrays.asList(paths.split("/")));
    }

    public void setBody(String body){
        this.body = nodeHelper.stringToNode(body);
    }

    public void setBody(Node node){
        this.body = node;
    }

    public String getBaseUri() {
        if (protocolExists() && hostExists()) { return getProtocol() + "://" + getHost(); }
        else { throw new IllegalArgumentException("No se pudo obtener el protocolo o el host\n"); }
    }

    public boolean protocolExists() { return this.protocol != null; }

    public boolean hostExists() { return this.host != null; }

    public String getProtocol() { return protocol; }

    public String getHost() {
        return this.host;
    }

    public void addHeader(String headerName, String headerValue) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(headerName, headerValue);
    }

    public void setUrl(String url) {
        try {
            String[] parts = url.split("://", 2);
            setHost(parts[1]);
            setProtocol(parts[0]);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("El valor <" + url + "> no parece ser una url válida");
        }
    }

    public void setHost(String valor) { this.host = valor; }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public RequestMethods getMethod() {
        if(method!=null) return method;
        else { throw new IllegalArgumentException("No se pudo obtener el Method\n"); }
    }

    public String getPath() {
        if(this.path !=null) return String.join("/",this.path);
        else { throw new IllegalArgumentException("No se pudo obtener el Path\n"); }
    }

    public String getRequest() {
        String pathString = String.join("/", this.path);
        String requestString = this.protocol + "://" + this.host + "/" + pathString;
        if(!bodyExists()) {
            return requestString;
        }else {
            return requestString + buildRequestBody();
        }
    }

    private String buildRequestBody() {
        return String.format("\nBody: %s", getBodyString());
    }

    public boolean headersExists() { return this.headers != null; }

    public HashMap<String, String> getHeaders() {
        return this.headers;
    }

    public String getName() {
        return this.name;
    }

    public boolean bodyExists() { return this.body != null; }

    /**
     * Método para obtener el valor de un campo o nodo del body de un request
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

    public Node getBody() {
        if (this.body == null || "".equals(this.body)) {
            throw new RuntimeException("El body esperado est\u00E1 vac\u00EDo");
        }
        return this.body;
    }

    public String getBodyString() {
        return nodeHelper.nodeToString(getBody());
    }

    public void removeBody(){ this.body = null; }

    /**
     * Metodo para borrar un campo o nodo del body de un request
     * @param campo
     */
    public void removeFromBody(String campo){
        Node node = nodeHelper.getNode(campo, this.body);
        body.removeChild(node);
    }

    /**
     * Metodo para setear el valor de un campo del body de un request
     * @param campo
     * @param valor
     */
    public void setBody(String campo , Object valor){
        Node node = nodeHelper.getNode(campo, this.body);
        node.setTextContent(String.valueOf(valor));
    }


}
