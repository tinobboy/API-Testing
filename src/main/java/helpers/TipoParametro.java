package helpers;

public enum TipoParametro {
    NUMERICO_LIMITADO("numerico_limitado"),
    STRING("string");

    TipoParametro(String tipo) {
        this.descripcion = tipo;
    }

    private final String descripcion;

    public String getDescripcion(){return descripcion;}


    @Override
    public String toString() {
        return getDescripcion();
    }

}
