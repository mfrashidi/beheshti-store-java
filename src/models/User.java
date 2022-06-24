package models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import exceptions.RepeatedPhoneNumber;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static utils.encryption.decrypt;
import static utils.encryption.encrypt;

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
        try {
            this.password = encrypt(password);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
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
        try {
            return decrypt(password);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPassword(String password) {
        try {
            this.password = encrypt(password);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
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

    public void addAddress(Product product) {
        favoriteProducts.add(product);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static User fromJson(String json) {
        return new Gson().fromJson(json, User.class);
    }
}
