package ict.util;

 public class Converter {

    public double toFahrenheit(double v) {
        double f = 32 + 9 * v / 5;
        return f;
    }

    public double toCelsius(double v) {
        double c = (v - 32) * 5 / 9;
        return c;
     }
}
