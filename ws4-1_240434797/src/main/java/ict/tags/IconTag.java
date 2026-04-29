package ict.tags;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class IconTag extends SimpleTagSupport {

    private String message = "Taglib is good";
    private String color = "00AAFF";

    public void setMessage(String message) {
        this.message = message;
    }

    public void setColor(String color) {
        if (color != null && !color.isEmpty()) {
            this.color = color;
        }
    }

    @Override
    public void doTag() {
        try {
            String safeColor = color.replaceAll("[^0-9A-Fa-f]", "");
            if (safeColor.isEmpty()) {
                safeColor = "00AAFF";
            }
            String text = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            String url = "https://img.shields.io/badge/" + text + "-" + safeColor;
            getJspContext().getOut().print("<img src=\"" + url + "\" alt=\"" + message + "\" /><br/>");
        } catch (IOException e) {
            throw new RuntimeException("Error rendering icon tag", e);
        }
    }
}
