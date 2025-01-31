package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FechaUtils {

    /**
     * M&eacute;todo que recibe un string con una fecha en formato yyyyMMdd y la transforma a un objeto {@link Date}
     * @param fechaAFormatear
     * @return
     */
    public Date getFechaFormateada(String fechaAFormatear){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date fechaARetornar = null;
        try{
            fechaARetornar = formatter.parse(fechaAFormatear);
        }
        catch (ParseException e){
            new IllegalArgumentException("Error en el formateo de la fecha. " + e);
        }
        return fechaARetornar;
    }
}
