package models;

import exceptions.ProductDoesNotExists;
import exceptions.UserDoesNotExists;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Order {
    private Date orderTime;
    private ArrayList<String> products;
    private String productID;
    private int totalPrice = 0;
    private int addressIndex;
    private String userID;

    public Order(String userID, ArrayList<String> products, int addressIndex) throws ProductDoesNotExists {
        this.orderTime = new Date();
        this.userID = userID;
        this.products = products;
        for (String id : products){
            this.totalPrice = Product.getProductByID(id).getPrice();
        }
        this.addressIndex = addressIndex;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<Product> getProducts() throws ProductDoesNotExists {
        ArrayList<Product> productsObjects = new ArrayList<>();
        for(String id : this.products)
            productsObjects.add(Product.getProductByID(id));
        return productsObjects;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Address getAddress() throws UserDoesNotExists {
        return User.getUserByID(userID).getAddresses().get(addressIndex);
    }

    public void setAddress(int addressIndex) {
        this.addressIndex = addressIndex;
    }

    public User getUser() throws UserDoesNotExists {
        return User.getUserByID(userID);
    }

    public void setUser(String userID) {
        this.userID = userID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductID() {
        return productID;
    }

    public static String getRandomOrderID(){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < 10){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, 10);
    }
}
