package helpers;

public class StringHelper {

    public static String formatText(String message, Object var){
        return String.format(message,var);
    }

    public static String formatText(String message, Object var1, Object var2){
        return String.format(message,var1,var2);
    }

    public static String formatText(String message, Object var1, Object var2,Object var3){
        return String.format(message,var1,var2,var3);
    }

    public static String formatText(String message, Object var1, Object var2,Object var3,Object var4){
        return String.format(message,var1,var2,var3,var4);
    }
}
