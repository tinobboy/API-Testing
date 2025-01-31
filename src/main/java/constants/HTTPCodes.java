package constants;

/**
 * Contiene informacion sobre los codigos de estado que el servicio puede responder
 *
 * @author avaccaro
 * @version 0.1.0
 */

public class HTTPCodes {
    private String descripcion;
    private int code;


    /**
     * Constructor de la clase
     *
     * @param descripcion Descripcion del mensaje que devuelve el servicio como respuesta a una peticion
     * @param code        StatusCode que devuelve el servicio como respuesta a una peticion
     */
    public HTTPCodes(String descripcion, int code) {
        this.descripcion = descripcion;
        this.code = code;
    }

    /**
     * Metodo que recibe como parametro un entero que representa un codigo de estado de la respuesta del servicio
     *
     * @param code Codigo de estado del servicio
     * @return Objeto que contiene informacion del codigo de estado recibido
     */
    public static HTTPCodes getHTTPCode(int code) {
        String descripcion = null;
        try {
            descripcion = Codes.descriptionOf(code);
        } catch (Exception e) {
            descripcion = "Codigo de status sin descripcion";
        }

        return new HTTPCodes(descripcion, code);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCode() {
        return code;
    }

    public static String descriptionOf(int code) {
        return getHTTPCode(code).getDescripcion();
    }

    /**
     * Enum que contiene todos los codigos de error y sus respectivas descripciones
     *
     * @author avaccaro
     * @version 0.1.0
     */
    public enum Codes {
        CODE_200("OK", 200),
        CODE_201("Created.", 201),
        CODE_202("Accepted", 202),
        CODE_203("Non-Authoritative Information", 203),
        CODE_204("No Content.", 204),
        CODE_205("Reset Content.", 205),
        CODE_206("Partial Content.", 206),
        CODE_207("Multi-Status.", 207),
        CODE_208("Already Reported.", 208),
        CODE_304("El servidor no responde.", 304),
        CODE_400("Bad Request", 400),
        CODE_401("Not Authorized.", 401),
        CODE_403("Forbidden.", 403),
        CODE_404("Not Found", 404),
        CODE_405("Method not allowed", 405),
        CODE_406("Not Acceptable.", 406),
        CODE_407("Proxy Authentication Required.", 407),
        CODE_408("Request Timeout.", 408),
        CODE_409("Conflict.", 409),
        CODE_412("Failed Precondition", 412),
        CODE_424("Failed Dependency.", 424),
        CODE_500("Internal Server Error.", 500),
        CODE_501("Not Implemented.", 501),
        CODE_503("Service Unavailable.", 503),
        CODE_504("Gateway Timeout.", 504),
        CODE_505("HTTP Version Not Supported.", 505),
        CODE_511("Network Authentication Required", 511);


        private String description;
        private int code;

        Codes(final String description, int code) {
            this.description = description;
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static String descriptionOf(int statusCode) {

            return getHTTPCode(statusCode).getDescription();
        }

        /**
         * Metodo que recibe un codigo de estado y devuelve un valor del enum Codes que coincida
         *
         * @param code Codigo de estado a buscar
         * @return Valor del enum Codes que coincide con el parametro <code>code</code>
         */
        public static Codes getHTTPCode(int code) {
            Codes codes = null;
            for (Codes statusCode : values()) {
                if (statusCode.code == (code)) {
                    codes = statusCode;
                }
            }
            return codes;
        }
    }
}