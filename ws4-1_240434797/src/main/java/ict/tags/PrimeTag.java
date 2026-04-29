package ict.tags;

import java.io.IOException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class PrimeTag extends SimpleTagSupport {

    private int min;
    private int max;
    private String tagType;

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    private boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void doTag() {
        try {
            if ("simple".equalsIgnoreCase(tagType)) {
                for (int i = min; i <= max; i++) {
                    if (isPrime(i)) {
                        getJspContext().getOut().print(i + " ");
                    }
                }
            } else if ("table".equalsIgnoreCase(tagType)) {
                int count = 0;
                getJspContext().getOut().println("<table border='1' cellpadding='4'><tr>");
                for (int i = min; i <= max; i++) {
                    if (isPrime(i)) {
                        getJspContext().getOut().print("<td>" + i + "</td>");
                        count++;
                        if (count % 10 == 0) {
                            getJspContext().getOut().println("</tr><tr>");
                        }
                    }
                }
                getJspContext().getOut().println("</tr></table>");
            } else {
                getJspContext().getOut().println("No such type");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error rendering prime tag", e);
        }
    }
}
