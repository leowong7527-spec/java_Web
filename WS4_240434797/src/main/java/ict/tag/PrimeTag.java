/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.tag;

import java.io.IOException;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/**
 *
 * @author a1
 */
public class PrimeTag extends SimpleTagSupport{

    int min;
    int max;
    String tagtype;

    @Override
    public void doTag() {
        try {
            JspWriter out = pageContext.getOut();
            if ("simple".equalsIgnoreCase(tagType)) {
                // display the simple format 
            } else if ("table".equalsIgnoreCase(tagType)) {
                // display the  table format 
            } else {
                out.println("No such type");
            }
        } catch (IOException ioe) {
            System.out.println("Error generating prime: " + ioe);
        }
    }
}
