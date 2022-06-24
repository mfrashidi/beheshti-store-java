package models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import database.DBHandler;
import exceptions.RepeatedPhoneNumber;
import exceptions.UserAlreadyExists;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

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

    public User(String name, String phoneNumber, String password, String userID)
            throws RepeatedPhoneNumber, UserAlreadyExists {
        this.name = name;
        if (!this.isUniquePhoneNumber()) throw new RepeatedPhoneNumber();
        this.phoneNumber = phoneNumber;
        try {
            this.password = encrypt(password);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        this.userID = userID;
        if (DBHandler.getUsers().contains(this)) throw new UserAlreadyExists();
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

    public static String getRandomUserID(){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < 10){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, 10);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userID.equals(user.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }
}
