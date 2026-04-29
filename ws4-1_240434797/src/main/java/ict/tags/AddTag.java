package ict.tags;

import java.io.IOException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class AddTag extends SimpleTagSupport {

    private int num1 = 0;
    private int num2 = 0;

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    @Override
    public void doTag() {
        try {
            getJspContext().getOut().print(num1 + num2);
        } catch (IOException e) {
            throw new RuntimeException("Error rendering add tag", e);
        }
    }
}
