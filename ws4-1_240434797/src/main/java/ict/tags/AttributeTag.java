package ict.tags;

import java.io.IOException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class AttributeTag extends SimpleTagSupport {

    private String message = "Default Message";

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void doTag() {
        try {
            getJspContext().getOut().print("Attribute example: " + message);
        } catch (IOException e) {
            throw new RuntimeException("Error rendering attribute tag", e);
        }
    }
}
