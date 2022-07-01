package models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import database.DBHandler;
import exceptions.OrderDoesNotExists;
import exceptions.RepeatedPhoneNumber;
import exceptions.UserAlreadyExists;
import exceptions.UserDoesNotExists;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import static utils.Encryption.decrypt;
import static utils.Encryption.encrypt;

public class User {
    private String name;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("user_id")
    private String userID;
    @SerializedName("phone")
    private String phoneNumber;
    private String password;
    private String email;
    private ArrayList<Address> addresses = new ArrayList<>();
    @SerializedName("active_address")
    private int activeAddress = 0;
    @SerializedName("favorites")
    private Set<String> favoriteProducts = new HashSet<>();
    private ArrayList<String> cart = new ArrayList<>();
    private ArrayList<String> orders = new ArrayList<>();
    private String token;
    @SerializedName("profile_picture")
    private String profilePicture;

    public User(String name, String lastName, String phoneNumber, String email, String password)
            throws RepeatedPhoneNumber, UserAlreadyExists {
        this.name = name;
        this.lastName = lastName;
        if (!this.isUniquePhoneNumber(phoneNumber)) throw new RepeatedPhoneNumber();
        this.phoneNumber = phoneNumber;
        this.email = email;
        try {
            this.password = encrypt(password);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        this.userID = User.getRandomUserID();
        this.token = User.generateToken();
        if (DBHandler.getUsers().contains(this)) throw new UserAlreadyExists();
        profilePicture = "default_user";
    }

    private boolean isUniquePhoneNumber(String phoneNumber) {
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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

    public int getActiveAddress() {
        return activeAddress;
    }

    public void setActiveAddress(int activeAddress) {
        this.activeAddress = activeAddress;
    }

    public void removeAddress(int index) {
        addresses.remove(index);
    }

    public String getToken() {
        return token;
    }

    public void updateToken() {
        this.token = User.generateToken();
    }

    public Set<String> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void setFavoriteProducts(Set<String> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }

    public void addFavoriteProduct(String product) {
        favoriteProducts.add(product);
    }

    public void removeFavoriteProduct(String product) {
        favoriteProducts.remove(product);
    }

    public ArrayList<Order> getOrders() throws OrderDoesNotExists {
        ArrayList<Order> orders = new ArrayList<>();
        for (String orderID : this.orders) orders.add(Order.getOrderByID(orderID));
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = (ArrayList<String>) orders.stream().map(Order::getOrderID).collect(Collectors.toList());
    }

    public void addOrder(Order order) {
        orders.add(order.getOrderID());
    }

    public ArrayList<String> getCart() {
        return cart;
    }

    public void setCart(ArrayList<String> cart) {
        this.cart = cart;
    }

    public void addToCart(String productID) {
        cart.add(productID);
    }

    public void removeFromCart(String productID) {
        cart.removeAll(Collections.singleton(productID));
    }

    public void removeFromCartOnce(String productID) {
        cart.remove(productID);
    }

    public String toJson(boolean withPassword) {
        if (withPassword)
            return new Gson().toJson(this);
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

    public static String generateToken(){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < 64){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, 64);
    }

    public static User getUserByID(String userID) throws UserDoesNotExists {
        for (User user : DBHandler.getUsers())
            if (user.getUserID().equals(userID))
                return user;
        throw new UserDoesNotExists();
    }

    public static User getUserByToken(String token) throws UserDoesNotExists {
        for (User user : DBHandler.getUsers())
            if (user.getToken().equals(token))
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

    public static String authenticate(String token) {
        ArrayList<User> users = DBHandler.getUsers();
        for (User user : users) {
            if (user.getToken().equals(token))
                return user.getUserID();
        }
        return "FAILED";
    }

    public void save() {
        DBHandler.removeUser(this);
        DBHandler.addUser(this);
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
