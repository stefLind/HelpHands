package app.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class StringUtil {

    public static boolean isNullOrBlank(String string) {
        return string == null || string.isBlank();
    }

    public static boolean isNotNullOrBlank(String string) {
        return !isNullOrBlank(string);
    }

    public static boolean isAllNullOrBlank(String... strings) {
        return Arrays.stream(strings).allMatch(StringUtil::isNullOrBlank);
    }
}
