package ict.tags;

import java.io.IOException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class ExampleTag extends SimpleTagSupport {

    @Override
    public void doTag() {
        try {
            getJspContext().getOut().print("Custom tag example (ExampleTag)");
        } catch (IOException e) {
            throw new RuntimeException("Error rendering example tag", e);
        }
    }
}
