package helpers;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enum que describe la manera en la que se va a modificar un valor.
 * Estas descripciones se utilizan en las tablas de ejemplos de los archivos .feature de manera estandarizada
 * con el fin de tener una correspondencia entre los métodos para modificar valores y los steps escritos en
 * Gherkin
 * @author lcmoreno
 */
public enum TipoDescripcionParametros {
    CON_LETRAS("con letras"),
    CON_GUIONES("con guiones"),
    CON_DECIMALES("con decimales"),
    CON_VALOR_NEGATIVO("con valor negativo"),
    CON_CARACTERES_ESPECIALES("con caracteres especiales"),
    CON_MENOS_DE_N("con menos de [0-9]+ [a-z]+"),
    CON_MAS_DE_N("con mas de [0-9]+ [a-z]+"),
    CON_N("con [0-9]+ [a-z]+"),
    CON_PUNTOS("con puntos"),
    CON_VALOR_CERO("con valor cero"),
    CON_NUMEROS("con numeros"),
        CON_NUMEROS_UNICAMENTE("con numeros unicamente"),
    CON_DECIMALES_CON_PUNTOS("con decimales con punto"),
    CON_ESPACIO("con espacio"),
    CON_MINUSCULAS("con minusculas");


    TipoDescripcionParametros(String tipo) {
        this.descripcion = tipo;
    }

    private String descripcion;

    public String getDescripcion(){return descripcion;}

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    /**
     * Método que recibe un string con una descripcion, y la evalúa devolviendo un item del enum que coincida
     * @param descripcion
     * @return objeto de enum del tipo TipoDescripcionParametros
     */
    public static TipoDescripcionParametros fromDescripcion(String descripcion) {
        for (TipoDescripcionParametros tipoDescripcionParametros : TipoDescripcionParametros.values()) {
            CON_MENOS_DE_N.setDescripcion("con menos de [0-9]+ [a-z]+");
            CON_MAS_DE_N.setDescripcion("con mas de [0-9]+ [a-z]+");
            Pattern pattern = Pattern.compile(tipoDescripcionParametros.descripcion);
            Matcher matcher = pattern.matcher(descripcion);
            if (matcher.matches()) {
                tipoDescripcionParametros.setDescripcion(descripcion);
                return tipoDescripcionParametros;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
}
