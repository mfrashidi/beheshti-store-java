package models;

import com.google.gson.annotations.SerializedName;
import exceptions.RepeatedPhoneNumber;

import java.util.ArrayList;

public class User {
    private String name;
    @SerializedName("user_id")
    private String userID;
    @SerializedName("phone")
    private String phoneNumber;
    private String password;
    private ArrayList<Address> addresses = new ArrayList<>();
    @SerializedName("favorites")
    private ArrayList<Product> favoriteProducts = new ArrayList<>();
    private ArrayList<Order> orders = new ArrayList<>();

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

    public void addAddress(Address address) {
        addresses.add(address);
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
