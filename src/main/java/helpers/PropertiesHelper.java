package helpers;

public class PropertiesHelper {


    private boolean doesPropertyExist(String propertyName) {
        return  isThePropertyNull(propertyName) ? true : false;

    }

    private boolean isThePropertyNull(String propertyName) {
        return System.getProperty(propertyName) != null;
    }
}
