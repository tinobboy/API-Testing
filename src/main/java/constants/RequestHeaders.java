package constants;

public enum RequestHeaders {

    AUTHORIZATION("Authorization"),
    CONTENT_TYPE("Content-Type"),
    KEEP_ALIVE("Keep-Alive");

    private final String description;

    RequestHeaders(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
