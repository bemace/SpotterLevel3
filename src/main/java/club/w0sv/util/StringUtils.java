package club.w0sv.util;

public class StringUtils {
    private StringUtils() {}

    /**
     * Returns {@code input} with any leading or trailing whitespace removed.
     * Returns {@code null} if {code input == null} or {@code input} is only whitespace.
     * @param input
     * @return
     */
    public static String trim(String input) {
        if (input == null)
            return null;
        
        input =  input.trim();
        return input.length() == 0 ? null : input;
    }

    /**
     * Returns {@code input} as an Integer, or {@code null} if {@code input} was null or an all-whitespace string.
     * @param input
     * @return
     * @throws NumberFormatException if the string contains non-whitespace characters and cannot be parsed as an Integer.
     */
    public static Integer parseInt(String input) {
        input = trim(input);
        return input == null ? null : Integer.parseInt(input);
    }
}
