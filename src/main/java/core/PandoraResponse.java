package core;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static helpers.JsonNodeHelper.getStringValueFromJson;
import static helpers.ObjectMapperHelper.*;

public class PandoraResponse {

    private int statusCode;
    private long time;
    private JsonNode body;
    private Map<String,String> cookies;
    private HashMap<String,String> headers;
    private String stringBody;

    public PandoraResponse(Response response) {
        setStatusCode(response.statusCode());
        setCookies(response.getCookies());
        setTime(response.getTime());
        if(isJSONValid(response.getBody().asString())) {
            setBody(createJsonNodeFromString(response.getBody().asPrettyString()));
        }else{
            setBody(response.getBody().asPrettyString());
        }
        setHeaders(response.getHeaders());
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public JsonNode getBody() {
        return body;
    }

    public void setBody(JsonNode body) {
        this.body = body;
    }

    public void setBody(String stringBody) {
        this.stringBody = stringBody;
    }

    public String getBodyString() {
        return stringBody;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    private void setHeaders(Headers headers) {
        HashMap<String, String> headersMap = new HashMap<>();
        List<Header> headerList = headers.asList();
        for (Header header : headerList){
            headersMap.put(header.getName(), header.getValue());
        }
        this.headers = headersMap;
    }

    private String getBodyFromResponse(ResponseBody responseBody) {
        return StringUtils.deleteWhitespace(responseBody.asString());
    }

    public String getContentType() {
        return getHeaders().containsKey("Content-Type") ?
                getHeaders().get("Content-Type")
                : getHeaders().get("content-type");
    }

    public boolean contentTypeExists(){
        return getHeaders().containsKey("Content-Type") || getHeaders().containsKey("content-type");
    }

    /**
     * Metodo para obtener el valor de un nodo del body de un request
     * @param node
     */
    public String getValueFromBody(String node){
        return getStringValueFromJson(node, this.body);
    }

    public JsonNode getNodeFromBody(String node){
        return body.at("/"+node);
    }
}
