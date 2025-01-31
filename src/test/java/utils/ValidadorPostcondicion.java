package utils;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.util.Date;

public class ValidadorPostcondicion {

    private JsonNode node;

    public ValidadorPostcondicion(JsonNode node){
        this.node=node;
    }

    public ValidadorPostcondicion(){

    }

    @Step("Se valida el campo {campo} con valor: {valorEsperado}")
    public ValidadorPostcondicion compararCampoInt(String campo,int valorEsperado) {
        validarExistenciaCampoInt(campo);
        Assertions.assertEquals(valorEsperado, node.at("/" + campo).intValue(), "El valor del campo '"+ campo +"' de la respuesta no coincide con el esperado");
        return this;
    }

    @Step("Se valida el campo {campo} con valor: {valorEsperado}")
    public ValidadorPostcondicion compararCampoString(String campo, String valorEsperado) {
        validarExistenciaCampoString(campo);
        Assertions.assertEquals(valorEsperado,node.at("/"+campo).textValue(), "El valor del campo '"+ campo +"' de la respuesta no coincide con el esperado");
        return this;
    }

    @Step("Se valida el campo {campo} con valor: {valorEsperado}")
    public ValidadorPostcondicion compararCampoStringSinComa(String campo, String valorEsperado) {
        validarExistenciaCampoString(campo);
        String dato =node.at("/"+campo).textValue().replace(",","").replace("  ","");
        Assertions.assertEquals(valorEsperado, dato, "El valor del campo '"+ campo +"' de la respuesta no coincide con el esperado");
        return this;
    }

    @Step("Se valida el campo {campo} con valor: {valorEsperado}")
    public ValidadorPostcondicion compararCampoDate(String campo, Date valorEsperado){
        validarExistenciaCampoString(campo);
        Assertions.assertEquals(valorEsperado,new FechaUtils().getFechaFormateada(node.at("/"+campo).textValue()));
        return this;
    }

    @Step("Se valida el campo {campo} con valor: {valorEsperado}")
    private ValidadorPostcondicion compararCampoDoubleLimiteDecimales(String campo, double valorEsperado, int decimales) {
        validarExistenciaCampoDouble(campo);
        Assertions.assertEquals(MathUtils.round(valorEsperado, decimales), MathUtils.round(node.at("/" + campo).doubleValue(), decimales), "El valor del campo '"+ campo +"' de la respuesta no coincide con el esperado");
        return this;
    }

    @Step("Se valida el campo {campo} con valor: {valorEsperado}")
    public ValidadorPostcondicion compararCampoDouble(String campo, Double valorEsperado) {
        validarExistenciaCampoDouble(campo);
        Assertions.assertEquals(valorEsperado, node.at("/" + campo).doubleValue(), "El valor del campo '"+ campo +"' de la respuesta no coincide con el esperado");
        return this;
    }

    @Step("Se valida el campo {campo} con valor: {valorEsperado}")
    public ValidadorPostcondicion compararCampoLong(String campo,long valorEsperado) {
        validarExistenciaCampoLong(campo);
        Assertions.assertEquals(valorEsperado, node.at("/" + campo).longValue(), "El valor del campo '"+ campo +"' de la respuesta no coincide con el esperado");
        return this;
    }

    @Step("Se valida el campo {campo} con valor: {valorEsperado}")
    public ValidadorPostcondicion compararCampoNumber(String campo, Number valorEsperado) {
        validarExistenciaCampoNumber(campo);
        JsonNode nodoCampo = node.at("/" + campo);
        if(nodoCampo.isDouble())
            Assertions.assertEquals(valorEsperado.doubleValue(), node.at("/" + campo).asDouble(), "El valor del campo '"+ campo +"' de la respuesta no coincide con el esperado");
        else if(nodoCampo.isLong())
            Assertions.assertEquals(valorEsperado.longValue(), node.at("/" + campo).asLong(), "El valor del campo '"+ campo +"' de la respuesta no coincide con el esperado");
        else
            Assertions.assertEquals(valorEsperado.intValue(), node.at("/" + campo).asInt(), "El valor del campo '"+ campo +"' de la respuesta no coincide con el esperado");
        return this;
    }

    @Step("Se valida el campo {campo} con valor: {valorEsperado}")
    public ValidadorPostcondicion compararCampoBoolean(String campo, Boolean valorEsperado) {
        validarExistenciaCampoBoolean(campo);
        Assertions.assertEquals(valorEsperado,node.at("/"+campo).booleanValue(), "El valor de la respuesta no coincide con el esperado");
        return this;
    }

    @Step("Se valida el campo {campo} con el valor: {valorEsperado}")
    public ValidadorPostcondicion compararCampoArray(String campo, JsonNode valorEsperado){
        validarExistenciaCampoArray(campo);
        Assertions.assertEquals(valorEsperado, node.at("/"+campo),"El valor de la respuesta no coincide con el esperado");
        return this;
    }
    @Step("Se valida que exista el campo {campo} y sea del tipo Array")
    public ValidadorPostcondicion validarExistenciaCampoArray(String campo) {
        Assertions.assertTrue(node.has(campo), "No se encuentra el campo " + campo + " en la respuesta");
        Assertions.assertTrue(node.at("/" + campo).isArray(), "El campo " + campo + " no es array. Se obtuvo: " + node.at("/" + campo).getNodeType().name());
        return this;
    }

    @Step("Se valida que exista el campo {campoNumber} y sea del tipo Number")
    public ValidadorPostcondicion validarExistenciaCampoNumber(String campoNumber){
        Assertions.assertTrue(node.has(campoNumber), "No se encuentra el campo " + campoNumber + " en la respuesta");
        Assertions.assertTrue(node.at("/" + campoNumber).isNumber(), "El campo " + campoNumber + " no es int. Se obtuvo: " + node.at("/" + campoNumber).getNodeType().name());
        return this;
    }
    @Step("Se valida que exista el campo {nombreCampo} y sea del tipo Object")
    public ValidadorPostcondicion validarExistenciaCampoObject(String nombreCampo){
        Assertions.assertTrue(node.has(nombreCampo), "No se encuentra el campo "+ nombreCampo +" en la respuesta");
        Assertions.assertTrue(node.at("/" + nombreCampo).isObject(), "El campo " + nombreCampo + " no es object. Se obtuvo: " + node.at("/" + nombreCampo).getNodeType().name());
        return this;
    }

    @Step("Se valida que exista el campo {nombreCampo} y sea del tipo Entero")
    public ValidadorPostcondicion validarExistenciaCampoInt(String nombreCampo) {
        Assertions.assertTrue(node.has(nombreCampo), "No se encuentra el campo "+ nombreCampo +" en la respuesta");
        Assertions.assertTrue(node.at("/" + nombreCampo).isInt(), "El campo " + nombreCampo + " no es int. Se obtuvo: " + node.at("/" + nombreCampo).getNodeType().name());
        return this;
    }

    @Step("Se valida que exista el campo {nombreCampo} y sea del tipo String")
    public ValidadorPostcondicion validarExistenciaCampoString(String nombreCampo){
        Assertions.assertTrue(node.has(nombreCampo), "No se encuentra el campo "+ nombreCampo +" en la respuesta");
        Assertions.assertTrue(node.at("/" + nombreCampo).isTextual(), "El campo " + nombreCampo + " no es string. Se obtuvo: " + node.at("/" + nombreCampo).getNodeType().name());
        return this;
    }

    @Step("Se valida que exista el campo {nombreCampo} y sea del tipo Boolean")
    public ValidadorPostcondicion validarExistenciaCampoBoolean(String nombreCampo){
        Assertions.assertTrue(node.has(nombreCampo), "No se encuentra el campo "+ nombreCampo +" en la respuesta");
        Assertions.assertTrue(node.at("/" + nombreCampo).isBoolean(), "El campo " + nombreCampo + " no es boolean. Se obtuvo: " + node.at("/" + nombreCampo).getNodeType().name());
        return this;
    }

    @Step("Se valida que exista el campo {nombreCampo} y sea del tipo Double")
    public ValidadorPostcondicion validarExistenciaCampoDouble(String nombreCampo){
        Assertions.assertTrue(node.has(nombreCampo), "No se encuentra el campo "+ nombreCampo +" en la respuesta");
        if (!node.at("/" + nombreCampo).asText().equals("0")) {
            Assertions.assertTrue(node.at("/" + nombreCampo).isDouble(), "El campo " + nombreCampo + " no es double. Se obtuvo: " + node.at("/" + nombreCampo).getNodeType().name());
        }
        return this;
    }

    @Step("Se valida que exista el campo {nombreCampo} y sea del tipo long")
    public ValidadorPostcondicion validarExistenciaCampoLong(String nombreCampo) {
        Assertions.assertTrue(node.has(nombreCampo), "No se encuentra el campo "+ nombreCampo +" en la respuesta");
        if (!node.at("/" + nombreCampo).asText().equals("0")) {
            Assertions.assertTrue(node.at("/" + nombreCampo).isLong(), "El campo " + nombreCampo + " no es long. Se obtuvo: " + node.at("/" + nombreCampo).getNodeType().name());
        }
        return this;
    }

    @Step("Se compara el valor del campo '{campo}' esperado '{esperado}' con el valor obtenido '{obtenido}'")
    public ValidadorPostcondicion compararCampos(String campo, Object esperado, Object obtenido){
        Assertions.assertEquals(esperado,obtenido,"El valor obtenido del campo '" + campo + "' no coincide con el esperado");
        return this;
    }

    @Step("Se compara que el valor del campo '{campo}' obtenido '{obtenido}' contenga el valor esperado '{esperado}'")
    public ValidadorPostcondicion compararCamposContains(String campo, Object esperado, Object obtenido){
        Assertions.assertTrue(obtenido.toString().contains(esperado.toString()),
                "El valor esperado del campo '" + campo + "' no se encuentra dentro del obtenido");
        return this;
    }
}
