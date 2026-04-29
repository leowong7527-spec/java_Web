package ict.tags;

import java.io.StringWriter;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class HeadingTag extends SimpleTagSupport {

    private String bgColor;
    private String color = null;
    private String align = "CENTER";
    private String fontSize = "36";
    private String fontList = "Arial, Helvetica, sans-serif";
    private String border = "0";
    private String width = null;

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public void setFontList(String fontList) {
        this.fontList = fontList;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public void doTag() {
        try {
            PageContext pageContext = (PageContext) getJspContext();
            JspWriter out = pageContext.getOut();
            out.print("<table border='" + border + "' bgcolor='" + bgColor + "' align='" + align + "'");
            if (width != null) {
                out.print(" width='" + width + "'");
            }
            out.print("><tr><td>");
            out.print("<span style='font-size: " + fontSize + "px; font-family: " + fontList + "; ");
            if (color != null) {
                out.print("color: " + color + "; ");
            }
            out.print("'>");

            StringWriter sw = new StringWriter();
            if (getJspBody() != null) {
                getJspBody().invoke(sw);
                out.print(sw.toString());
            }

            out.print("</span></td></tr></table>");
        } catch (Exception ignored) {
        }
    }
}
