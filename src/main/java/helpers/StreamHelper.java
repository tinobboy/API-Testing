package helpers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamHelper {

    public static List<String> skipElementsOfStringArray(int amountToSkip, String[] elements) {
        return Arrays.stream(elements).skip(amountToSkip).collect(Collectors.toList());
    }
}
