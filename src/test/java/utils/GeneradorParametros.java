package utils;

import helpers.TipoDescripcionParametros;
import helpers.TipoParametro;
import org.junit.platform.commons.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneradorParametros {
    /**
     * Método que recibe un valor que se quiere modificar, el tipo de parámetro de ese valor
     * y una descripcion que indica de que manera se quiere modificar dicho valor. Hace uso del método
     * {@link #modificadorNumericoLimitado(String, String)}
     *
     * @param valor         valor a modificar
     * @param tipoParametro tipo del parametro que puede ser numerico, string o cualquiera definido en
     *                      el enum {@link helpers.TipoParametro}
     * @param descripcion   indica como se va a modificar el valor recibido. Dichas descripciones se encuentran
     *                      definidas en el enum {@link TipoDescripcionParametros}
     * @return Devuelve un string con el valor modificado segun los parámetros recibidos por el método
     */
    public String modificadorValorParametro(String valor, TipoParametro tipoParametro, String descripcion) {
        String resultado = "";
        switch (tipoParametro) {
            case NUMERICO_LIMITADO:
                if (TipoDescripcionParametros.fromDescripcion(descripcion) != null) {
                    resultado = modificadorNumericoLimitado(valor, descripcion);
                    break;
                } else
                    throw new IllegalArgumentException("No se puede transformar el valor para esa descripcion " + descripcion);
            case STRING:
                try{
                    resultado = modificadorString(valor, TipoDescripcionParametros.fromDescripcion(descripcion));
                }
                catch (Exception e){
                    throw new IllegalArgumentException("No se puede transformar el valor para esa descripcion " + descripcion);
                }
                break;
            default:
                //TODO
                break;
        }
        return resultado;
    }

    /**
     * Método que recibe un valor a modificar y una descripcion sobre como debe modificarlo.
     *
     * @param valor       valor a modificar
     * @param descripcion indica como se va a modificar el valor recibido. Dichas descripciones se encuentran
     *                    definidas en el enum {@link TipoDescripcionParametros}
     * @return el parametro valor modificado segun la descripción
     * @see TipoDescripcionParametros
     */
    private String modificadorNumericoLimitado(String valor, String descripcion) {
        String result = "";
        Pattern patternMenos = Pattern.compile("con menos de [0-9]+ [a-z]+");
        Matcher matcherMenos = patternMenos.matcher(descripcion);

        Pattern patternMas = Pattern.compile("con mas de [0-9]+ [a-z]+");
        Matcher matcherMas = patternMas.matcher(descripcion);

        Pattern patternEquals = Pattern.compile("con [0-9]+ [a-z]+");
        Matcher matcherEquals = patternEquals.matcher(descripcion);

        if (descripcion.equalsIgnoreCase(TipoDescripcionParametros.CON_LETRAS.getDescripcion())) {
            result = valor.replace(valor.charAt(valor.length() - 1), 'f');
        }
        if (descripcion.equalsIgnoreCase(TipoDescripcionParametros.CON_GUIONES.getDescripcion())) {
            result = valor.substring(0, 1) + '-' + valor.substring(1);
        }
        if (descripcion.equalsIgnoreCase(TipoDescripcionParametros.CON_DECIMALES.getDescripcion())) {
            result = valor + ",50";
        }

        if (descripcion.equalsIgnoreCase(TipoDescripcionParametros.CON_CARACTERES_ESPECIALES.getDescripcion())) {
            result = valor.substring(0, 1) + '$' + valor.substring(1);
        }
        if (descripcion.equalsIgnoreCase(TipoDescripcionParametros.CON_VALOR_CERO.getDescripcion())) {
            result = "0";
        }
        if (descripcion.equalsIgnoreCase(TipoDescripcionParametros.CON_VALOR_NEGATIVO.getDescripcion())) {
            result = "-" + valor;
        }
        if(descripcion.equalsIgnoreCase(TipoDescripcionParametros.CON_PUNTOS.getDescripcion())){
            result = valor + ".";
        }
        if (descripcion.equalsIgnoreCase(TipoDescripcionParametros.CON_NUMEROS.getDescripcion())) {
            result = valor.replace(valor.substring(0, 1), "3");
            return result;
        }
        if(descripcion.equalsIgnoreCase(TipoDescripcionParametros.CON_ESPACIO.getDescripcion())){
            result = valor.replace(valor.substring(2, 3), "%20");
        }
        if(descripcion.equalsIgnoreCase(TipoDescripcionParametros.CON_MINUSCULAS.getDescripcion())){
            result = valor.toLowerCase();
        }
        if(descripcion.equalsIgnoreCase(TipoDescripcionParametros.CON_NUMEROS.getDescripcion())){
            result = valor.replace(valor.substring(0, 1), "3");
        }
        if(descripcion.equalsIgnoreCase(TipoDescripcionParametros.CON_NUMEROS_UNICAMENTE.getDescripcion())){
            result = String.valueOf(2323);
        }
        if (matcherMenos.matches()) {
            int lenght = Integer.parseInt(descripcion.substring(13, 15).trim());
            result = valor.substring(0, lenght -1);
        }
        if (matcherMas.matches()) {
            int lenght = Integer.parseInt(descripcion.substring(11, 13).trim());
            result = new MathUtils().generarEnteroAleatorioSuperiorAlLimiteNumerico(lenght + 1);
        }
        if (matcherEquals.matches()) {
            int lenght = Integer.parseInt(descripcion.substring(4, 6).trim());
            result = new MathUtils().generarEnteroAleatorioSuperiorAlLimiteNumerico(lenght);
        }
        return result;
    }

    /**
     * Método que genera automaticamente una cadena alfanumérica de una longitud
     * especificada N
     * @param longitud Longitud N de la cadena alfanumerica
     * @return cadena alfanumerica de longitud N
     */
    public String generarCadenaAleatoria(int longitud){
        String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String cadena = "";
        for (int x = 0; x < longitud; x++) {
            int indiceAleatorio = new MathUtils().generarEnteroAleatorioEntreValores(banco.length() - 1, 0);
            char caracterAleatorio = banco.charAt(indiceAleatorio);
            cadena += caracterAleatorio;
        }
        return cadena;
    }

    /**
     * Método que recibe un valor a modificar y una descripcion sobre como debe modificarlo.
     * @param valor valor a modificar
     * @param tipoDescripcionParametros como se va a modificar el valor recibido. Dichas descripciones se encuentran
     *                    definidas en el enum {@link TipoDescripcionParametros}
     * @return el parametro valor modificado segun la descripción
     * @see TipoDescripcionParametros
     */
    private String modificadorString(String valor, TipoDescripcionParametros tipoDescripcionParametros){
        String result = "";
        switch (tipoDescripcionParametros){
            case CON_CARACTERES_ESPECIALES:
                result = "@!" + valor.substring(1);
                return result;
            case CON_NUMEROS:
                result = valor.replace(valor.substring(0, 1), "3");
                return result;
            default:
                return result;
        }
    }
}