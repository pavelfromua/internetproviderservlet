package my.project.internetprovider.util;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Language {
    public static Locale getLocale(String lang) {
        if ("ru".equals(lang))
            return new Locale("ru", "RU");
        if ("ua".equals(lang))
            return new Locale("ua", "UA");

        return new Locale("en", "US");
    }

    public static String getLocalizedMessage(String message, Locale language) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", language);

        return bundle.getString(message);
    }

    public static Map<String, String> getLocalizedMessages(Map<String, String> messages, Locale language) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", language);

        return messages.entrySet().stream().collect(Collectors
                .toMap(m->m.getKey(), m-> bundle.getString(m.getValue())));
    }
}
