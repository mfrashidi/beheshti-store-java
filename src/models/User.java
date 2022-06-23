package models;

import exceptions.RepeatedPhoneNumber;

import java.util.ArrayList;

public class User {
    private String name;
    private String userID;
    private String phoneNumber;
    private String password;
    private ArrayList<Address> addresses;
    private ArrayList<Product> favoriteProducts;
    private ArrayList<Order> orders;

    public User(String name, String phoneNumber, String password) throws RepeatedPhoneNumber {
        this.name = name;
        if (!this.isUniquePhoneNumber()) throw new RepeatedPhoneNumber();
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    private boolean isUniquePhoneNumber() {
        return true; // TODO: Handle repeated phone number
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    public ArrayList<Product> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void setFavoriteProducts(ArrayList<Product> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}
