package core;

import com.fasterxml.jackson.databind.JsonNode;
import constants.RequestHeaders;
import constants.RequestMethods;

import java.util.*;

import static helpers.StringHelper.*;
import static helpers.JsonNodeHelper.*;
import static org.apache.commons.lang3.StringUtils.*;
import static java.lang.String.*;

public class RequestSpecification {

    private HashMap<String, String> headers;
    private HashMap<String, List<String>> parameters;
    private List<String> paths;
    private String host;
    private String protocol;
    private RequestMethods method;
    private JsonNode body;


    public RequestSpecification(String url, RequestMethods method) {
        setBaseUri(url);
        setMethod(method);
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<RequestHeaders, String> headers) {
        initHeaders();
        HashMap<String, String> hashMapHeaders = new HashMap<>();
        for(HashMap.Entry<RequestHeaders, String> header : headers.entrySet()){
            hashMapHeaders.put(header.getKey().getDescription(), header.getValue());
        }
        this.headers.putAll(hashMapHeaders);
    }

    public void setHeaders(RequestHeaders headerName, String headerValue) {
        initHeaders();
        this.headers.put(headerName.getDescription(), headerValue);
    }

    public void setHeaders(String headerName, String headerValue) {
        initHeaders();
        this.headers.put(headerName, headerValue);
    }

    public void removeHeader(RequestHeaders header) {
        this.headers.remove(header.getDescription());
    }

    public void removeHeader(String header) {
        this.headers.remove(header);
    }

    public void removeAllHeaders() {
        this.headers = null;
    }

    public HashMap<String, List<String>> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, List<String>> parameters) {
        initParameters();
        this.parameters.putAll(parameters);
    }

    public void setParameters(String parameterName, List<String> parameterValues) {
        initParameters();
        this.parameters.put(parameterName, parameterValues);
    }

    public RequestSpecification setParameters(String parameterName, String parameterValue) {
        setParameters(parameterName, List.of(parameterValue));
        return this;
    }

    public void removeParameter(String parameterName) {
        this.parameters.remove(parameterName);
    }

    public void removeAllParameters() {
        this.parameters = null;
    }

    public List<String> getPaths() {
        return paths;
    }

    public String getFullPath() {
        List<String> params = new ArrayList<>();
        if (parametersExists())
            this.parameters.forEach((clave, valor) -> params.add(clave + "=" + valor.toString().replace("[", "").replace("]", "")));
        String pathString = "/"+String.join("/", this.paths);
        pathString = (params.isEmpty()) ? pathString : pathString+"?"+String.join("&", params);
        return pathString;
    }

    public void setPaths(List<String> paths) {
        initPaths();
        this.paths = paths;
    }

    public void setPaths(String path) {
        initPaths();
        setPaths(createPaths(path));
    }

    public void removeAllPaths() {
        this.paths = null;
    }

    public String getHost() {
        return host;
    }

    public String getProtocol() {
        return protocol;
    }

    private void setBaseUri(String url) {
        String[] partOfUrl = splitUrl(url);
        setProtocol(partOfUrl[0].toLowerCase());
        setHost(partOfUrl[1].toLowerCase());
    }

    public String getBaseUri() {
        return formatText("%s://%s", this.protocol, this.host);
    }

    public JsonNode getBody() {
        return body;
    }

    public String getBodyLikeString() { return bodyExists() ? valueOf(this.body) : ""; }

    public void setBody(JsonNode body) {
        this.body = body;
    }

    public String getValueFromBody(String node){
        return getStringValueFromJson(node,this.body);
    }

    public RequestSpecification setBody(String nodeName, Object nodeValue){
        this.body = isBodyNull() ?
                setNodeInJson(nodeName,nodeValue)
                : setNodeInJson(nodeName,nodeValue,this.body);

        return this;
    }

    public void removeNodeFromBody(String node){
        this.body = removeNodeFromJson(node,this.body) ;
    }

    public void removeAllBody(){
        this.body = null ;
    }

    public RequestMethods getMethod() {
        return method;
    }

    public boolean pathExists() {return !isPathNull();}

    public boolean parametersExists() {return !isParametersNull();}

    public boolean bodyExists() {return !isBodyNull();}

    private void setMethod(RequestMethods method) {
        this.method = method;
    }

    private void setHost(String host) {
        this.host = host;
    }

    private void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    private void initHeaders() {
        if (isHeadersNull()) this.headers = new HashMap<>();
    }

    private boolean isHeadersNull() {
        return this.headers == null;
    }

    private void initParameters() {
        if (isParametersNull()) this.parameters = new HashMap<>();
    }

    private boolean isParametersNull() {
        return this.parameters == null;
    }

    private void initPaths() {
        if (isPathNull()) this.paths = new ArrayList<>();
    }

    private boolean isPathNull() { return this.paths == null; }

    private boolean isValidPath(String path) {
        return path.startsWith("/") && path.length() > 2;
    }

    private List<String> createPaths(String path) {
        if (isValidPath(path)) {
            return Arrays.asList(split(path,"/").clone());
        } else {
            throw new IllegalArgumentException(formatText("%s is not a valid Path. The path must start with '/' and have at least 2 characters", path));
        }
    }

    private boolean isValidURL(String url) {
        return url.split("://").length == 2;
    }

    private String[] splitUrl(String url) {
        if (isValidURL(url)) {
            return url.split("://");
        } else {
            throw new IllegalArgumentException(formatText("%s is not a valid URL", url));
        }
    }

    private boolean isBodyNull() {
        return this.body == null;
    }
}
