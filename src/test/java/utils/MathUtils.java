package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathUtils {
    /**
     * Metodo que genera un entero aleatorio con una longitud superior al limite. El parametro
     * debe ser mayor a 0
     * @param longitud
     * @return
     */
    public String generarEnteroAleatorioSuperiorAlLimiteNumerico(int longitud){
        String banco = "123456789";
        String cadena = "";
        for (int x = 0; x < longitud; x++) {
            int indiceAleatorio = generarEnteroAleatorioEntreValores(banco.length() - 1, 0);
            char caracterAleatorio = banco.charAt(indiceAleatorio);
            cadena += caracterAleatorio;
        }
        return cadena;
    }
    /**
     * Metodo que genera un numero aleatorio entre un rango de valores especificado.
     * No se probo para valores negativos
     * @param numeroMaximo Valor maximo que puede tomar el numero aleatorio
     * @param numeroMinimo Valor minimo que puede tomar el numero aleatorio
     * @return Entero aleatorio entre numeroMinimo y numeroMaximo
     */
    public int generarEnteroAleatorioEntreValores(int numeroMaximo, int numeroMinimo){
        int aleatorio = (int) (Math.random() * (numeroMaximo + 1 - numeroMinimo)) + numeroMinimo;
        return aleatorio;
    }
    /**
     * Método que redondea un valor del tipo double hacia arriba en caso de acercarse a la unidad siguiente.
     * Ej1: Un valor 24.586 resultará en un número
     * 24.59 si es que el parámetro places es igual a 2.
     * Ej2: Un valor 24.5849 resultará en un número
     * 24.58 si es que el parámetro places es igual a 2.
     * @param value Valor a redondear
     * @param places Cantidad de decimales que se conservarán al redondear
     * @return Valor redondeado
     * @throws IllegalArgumentException
     * @since 21/04/2021
     * @author c00morenol
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Metodo que trunca el numero de decimales de un Double en una cantidad especificada.
     * @param numero Double a truncar
     * @param cantidadDecimales Cantidad de digitos decimales que se quieren conservar
     * @return String con el numero truncado
     */
    public String truncarDouble(Double numero, int cantidadDecimales){
        String formato = "#.";
        for(int i=0; i<cantidadDecimales; i++){
            formato = formato + "0";
        }
        DecimalFormat formatter = new DecimalFormat(formato);
        return formatter.format(numero);
    }

}
