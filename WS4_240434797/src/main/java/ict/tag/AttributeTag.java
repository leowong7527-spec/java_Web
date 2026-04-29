package ict.tag;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.*;

public class AttributeTag extends SimpleTagSupport {

    public void setMessage(String message1) {
        message = message1;
    }

    private String message = "Default Message";

    public void doTag() {
        try {
            PageContext pageContext = (PageContext) getJspContext();
            JspWriter out = pageContext.getOut();
            out.print("Attribute example " + message);
        } catch (IOException e) {
        }
    }

}
