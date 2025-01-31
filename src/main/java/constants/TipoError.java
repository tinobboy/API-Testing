package constants;

import java.util.Arrays;

public enum TipoError {
    BLOQUEANTE("ERROR BLOQUEANTE"),
    CRITICO("ERROR CRITICO"),
    NORMAL("ERROR MEDIO"),
    TRIVIAL("ERROR TRIVIAL");

    TipoError(String tipo) {
        this.descripcion = tipo;
    }

    private final String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Compara el codigo actual del requesty el codigo esperado y se devuelve el tipo de error correspondiente.
     * Si el codigo actual es 200 y el codigo esperado no coincide,se considera ERROR BLOQUEANTE.
     * Si el codigo actual es distinto de 200 y el codigo esperad, es 200 ,se considera ERROR BLOQUEANTE.
     * Si el codigo actual y esperado son distintos a 200, se considera  ERROR NORMAL.
     * En cualquier otro caso, se considera ERROR NORMAL
     *
     * @param actualCode
     * @param expectedCode
     * @return
     */
    public static TipoError getTipoErrorSegunCodigos(int actualCode, int expectedCode) {
        int[] errores = {400, 404, 405, 412};
        int[] success = {200, 201, 204};
        switch (actualCode) {
            case 200:
            case 204:
            case 201:
                if (Arrays.asList(success).contains(expectedCode)) return TipoError.CRITICO;
                if (Arrays.asList(errores).contains(expectedCode)) return TipoError.BLOQUEANTE;
                break;
            case 400:
            case 404:
            case 405:
            case 412:
                if (Arrays.asList(success).contains(expectedCode)) return TipoError.BLOQUEANTE;
                if (Arrays.asList(errores).contains(expectedCode)) return TipoError.NORMAL;
                break;
            default:
                break;
        }
        return TipoError.NORMAL;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
}
