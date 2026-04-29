package ict.util;

public class HtmlUtil {
    
    /**
     * Escapes HTML special characters to prevent XSS attacks
     * @param input The string to escape
     * @return The escaped string
     */
    public static String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }
}
