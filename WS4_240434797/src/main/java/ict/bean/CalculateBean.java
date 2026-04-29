/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.bean;

import ict.util.CalculateHelper;
import java.io.Serializable;


/**
 *
 * @author a1
 */
public class CalculateBean implements Serializable{
        // private properties
    private int value1;
    private int value2;
    private String operator;

    // default constructor
    public CalculateBean() {
    }

    // getters and setters
    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    // calculate method
    public int calculate() throws Exception {
        CalculateHelper helper = new CalculateHelper();
        int result = 0;
        if (null == operator) {
            throw new Exception("No operator is selected");
        } else switch (operator) {
            case CalculateHelper.ADD:
                result = helper.add(value1, value2);
                break;
            case CalculateHelper.SUBTRACT:
                result = helper.subtract(value1, value2);
                break;
            case CalculateHelper.MULTIPLY:
                result = helper.multiply(value1, value2);
                break;
            default:
                throw new Exception("No operator is selected");
        }
        return result;
    }
}
