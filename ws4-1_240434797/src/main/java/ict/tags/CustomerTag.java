package ict.tags;

import ict.bean.CustomerBean;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class CustomerTag extends SimpleTagSupport {

    private List<CustomerBean> customers;
    private String tagType;

    public void setCustomers(List<CustomerBean> customers) {
        this.customers = customers;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    @Override
    public void doTag() {
        try {
            JspWriter out = getJspContext().getOut();

            if (customers == null || customers.isEmpty()) {
                out.println("No customer data");
                return;
            }

            if ("simple".equalsIgnoreCase(tagType)) {
                for (CustomerBean c : customers) {
                    out.println(c.toString() + "<br/>");
                }
            } else if ("list".equalsIgnoreCase(tagType)) {
                for (CustomerBean c : customers) {
                    out.println("<ul>");
                    out.println("<li>Custid:" + c.getCustid() + "</li>");
                    out.println("<li>Name:" + c.getName() + "</li>");
                    out.println("<li>Tel:" + c.getTel() + "</li>");
                    out.println("<li>Age:" + c.getAge() + "</li>");
                    out.println("</ul>");
                }
            } else {
                out.println("No such type");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error rendering customer tag", e);
        }
    }
}
