package models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import database.DBHandler;
import exceptions.OrderDoesNotExists;
import exceptions.ProductDoesNotExists;
import exceptions.UserDoesNotExists;

import java.util.ArrayList;
import java.util.Random;

public class Order {
    @SerializedName("order_time")
    private Long orderTime;
    private ArrayList<String> products;
    private String productID;
    @SerializedName("total_price")
    private int totalPrice = 0;
    @SerializedName("address_index")
    private int addressIndex;
    @SerializedName("user_id")
    private String userID;
    @SerializedName("order_id")
    private String orderID;

    public Order(String userID, ArrayList<String> products, int addressIndex) throws ProductDoesNotExists {
        this.orderTime = System.currentTimeMillis() / 1000L;
        this.userID = userID;
        this.products = products;
        this.totalPrice = 0;
        for (String id : products){
            this.totalPrice += Product.getProductByID(id.split("@")[0]).getPrice() * Integer.parseInt(id.split("@")[1]);
        }
        this.addressIndex = addressIndex;
        this.orderID = getRandomOrderID();
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

    public Long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Long orderTime) {
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

    public String getOrderID() {
        return orderID;
    }

    public static Order getOrderByID(String orderID) throws OrderDoesNotExists {
        for (Order order : DBHandler.getOrders()) {
            if (order.getOrderID().equals(orderID)) {
                return order;
            }
        }
        throw new OrderDoesNotExists();
    }

    public static String getRandomOrderID(){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < 10){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, 10);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static Order fromJson(String json) {
        return new Gson().fromJson(json, Order.class);
    }
}
