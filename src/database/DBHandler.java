package database;

import models.Image;
import models.Order;
import models.Product;
import models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DBHandler {
    private final static String initPath = "";
    private final static String usersFile = initPath + "users.json";
    private final static String productsFile = initPath + "products.json";
    private final static String ordersFile = initPath + "orders.json";
    private final static String imagesFile = initPath + "images.json";

    private static void saveUsers(ArrayList<User> users) {
        try (FileWriter fileWriter = new FileWriter(usersFile)){
            for (User user : users)
                fileWriter.write(user.toJson(true) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(usersFile))){
            while (scanner.hasNextLine())
                users.add(User.fromJson(scanner.nextLine()));
        } catch (FileNotFoundException e) {
            try (FileWriter fileWriter = new FileWriter(usersFile)) {
                fileWriter.write("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return users;
    }

    public static void addUser(User user) {
        ArrayList<User> users = getUsers();
        if (!users.contains(user))
            users.add(user);
        saveUsers(users);
    }

    public static void removeUser(User user) {
        ArrayList<User> users = getUsers();
        users.remove(user);
        saveUsers(users);
    }

    private static void saveProducts(ArrayList<Product> products) {
        try (FileWriter fileWriter = new FileWriter(productsFile)){
            for (Product product : products)
                fileWriter.write(product.toJson() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(productsFile))){
            while (scanner.hasNextLine())
                products.add(Product.fromJson(scanner.nextLine()));
        } catch (FileNotFoundException e) {
            try (FileWriter fileWriter = new FileWriter(productsFile)) {
                fileWriter.write("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return products;
    }

    public static void addProducts(Product product) {
        ArrayList<Product> products = getProducts();
        if (!products.contains(product))
            products.add(product);
        saveProducts(products);
    }

    public static void removeProduct(Product product) {
        ArrayList<Product> products = getProducts();
        products.remove(product);
        saveProducts(products);
    }

    private static void saveOrders(ArrayList<Order> orders) {
        try (FileWriter fileWriter = new FileWriter(ordersFile)){
            for (Order order : orders)
                fileWriter.write(order.toJson() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Order> getOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(ordersFile))){
            while (scanner.hasNextLine())
                orders.add(Order.fromJson(scanner.nextLine()));
        } catch (FileNotFoundException e) {
            try (FileWriter fileWriter = new FileWriter(ordersFile)) {
                fileWriter.write("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return orders;
    }

    public static void addOrder(Order order) {
        ArrayList<Order> orders = getOrders();
        if (!orders.contains(order))
            orders.add(order);
        saveOrders(orders);
    }

    public static void removeOrder(Order order) {
        ArrayList<Order> orders = getOrders();
        orders.remove(order);
        saveOrders(orders);
    }

    private static void saveImages(ArrayList<Image> images) {
        try (FileWriter fileWriter = new FileWriter(imagesFile)){
            for (Image image : images)
                fileWriter.write(image.toJson() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Image> getImages() {
        ArrayList<Image> images = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(imagesFile))){
            while (scanner.hasNextLine())
                images.add(Image.fromJson(scanner.nextLine()));
        } catch (FileNotFoundException e) {
            try (FileWriter fileWriter = new FileWriter(imagesFile)) {
                fileWriter.write("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return images;
    }

    public static void addImage(Image image) {
        ArrayList<Image> images = getImages();
        if (!images.contains(image))
            images.add(image);
        saveImages(images);
    }

    public static void removeImage(Image image) {
        ArrayList<Image> images = getImages();
        images.remove(image);
        saveImages(images);
    }

}
