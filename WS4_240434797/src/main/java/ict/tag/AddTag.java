package ict.tag;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class AddTag extends SimpleTagSupport {
    private int num1;
    private int num2;

    // setters for attributes
    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    @Override
    public void doTag() {
        try {
            JspWriter out = getJspContext().getOut();
            out.print("The sum of " + num1 + " and " + num2 + " is " + (num1 + num2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
