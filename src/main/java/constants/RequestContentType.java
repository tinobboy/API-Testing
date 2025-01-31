package constants;

public enum  RequestContentType {

    JSON("application/json");

    private final String description;

    RequestContentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
