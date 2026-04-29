package ict.tag;

import java.io.IOException;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ExampleTag extends SimpleTagSupport {

    public void doTag() {
        try {
            PageContext pageContext = (PageContext) getJspContext();
            JspWriter out = pageContext.getOut();
            out.print("Custom tag example "   + "(Example Tag)");
        } catch (IOException ex) {
          ex.printStackTrace();
        }
 
    }
}
