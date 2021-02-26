package my.project.internetprovider.exception;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckException extends Exception {
    public CheckException(String message) {
        super(message);
    }

    public CheckException(Map<String, String> messages) {
        super(fromMultipleToSingleMessage(messages));
    }

    public static String fromMultipleToSingleMessage(Map<String, String> messages) {
        return messages.entrySet()
                .stream()
                .map(e -> e.toString())
                .collect(Collectors.joining(","));

    }

    public static Map<String, String> fromSingleToMultipleMessage(String message) {
        return Arrays.stream(message.split(","))
                .map(e -> e.split("="))
                .collect(Collectors.toMap(e -> e[0], e -> e[1]));
    }
}
