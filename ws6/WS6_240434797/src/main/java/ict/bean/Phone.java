package ict.bean;
import java.io.Serializable;

public class Phone implements Serializable {
    private String name;
    private String img;
    private double price;

    public Phone() {}
    public Phone(String name, String img, double price) {
        this.name = name;
        this.img = img;
        this.price = price;
    }

    public String getName() { return name; }
    public String getImg() { return img; }
    public double getPrice() { return price; }
}
