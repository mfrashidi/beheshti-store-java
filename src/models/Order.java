package models;

import java.util.ArrayList;
import java.util.Date;

public class Order {
    private Date orderTime;
    private ArrayList<Product> products;
    private int totalPrice;

    public Order(ArrayList<String> products, int totalPrice) {
        this.orderTime = new Date();
        this.products = new ArrayList<>();
        for (String id : products) this.products.add(Product.getProductByID(id));
        this.totalPrice = totalPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

}
