package constants;

public enum RequestMethods {

    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    private final String type;

    RequestMethods(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public static RequestMethods getRequestMethod(String method) {
        RequestMethods requestMethod = null;
        for (RequestMethods rms : values()) {
            if (rms.type.equals(method.toUpperCase())) {
                requestMethod = rms;
            }
        }
        return requestMethod;
    }
}
