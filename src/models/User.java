package models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import database.DBHandler;
import exceptions.RepeatedPhoneNumber;
import exceptions.UserAlreadyExists;
import exceptions.UserDoesNotExists;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static utils.Encryption.decrypt;
import static utils.Encryption.encrypt;

public class User {
    private String name;
    @SerializedName("user_id")
    private String userID;
    @SerializedName("phone")
    private String phoneNumber;
    private String password;
    private String email;
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
        for (User user : DBHandler.getUsers())
            if (user.getPhoneNumber().equals(phoneNumber))
                return false;
        return true;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        String json = new Gson().toJson(this);
        return json.replaceFirst(",\"password\":\".*?\"", "");
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

    public static User getUserByID(String userID) throws UserDoesNotExists {
        for (User user : DBHandler.getUsers())
            if (user.getUserID().equals(userID))
                return user;
        throw new UserDoesNotExists();
    }

    public static String authenticate(String phoneNumber, String password) {
        ArrayList<User> users = DBHandler.getUsers();
        for (User user : users) {
            if (user.getPhoneNumber().equals(phoneNumber) && user.getPassword().equals(password))
                return user.getUserID();
        }
        return "FAILED";
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

    @Override
    public String toString() {
        return name + " (" + userID + ")";
    }
}
