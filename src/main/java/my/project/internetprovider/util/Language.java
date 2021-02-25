package my.project.internetprovider.util;

import java.util.Locale;

public class Language {
    public static Locale getLocale(String lang) {
        if ("ru".equals(lang))
            return new Locale("ru", "RU");
        if ("ua".equals(lang))
            return new Locale("ua", "UA");

        return new Locale("en", "US");
    }
}
