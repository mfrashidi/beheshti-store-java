package network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.DBHandler;
import exceptions.RepeatedPhoneNumber;
import exceptions.UserDoesNotExists;
import models.*;
import utils.JUI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static utils.Converter.normalize;

public class Server extends Thread{
    private final int PORT = 4536;
    public static void main(String[] args) {
        new Server().start();
    }

    public void run() {
        try {
            JUI.changeColor(JUI.Colors.BOLD_GREEN);
            System.out.println("Server started...");
            JUI.changeColor(JUI.Colors.WHITE);
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String command = "";
        String response = "";
        User user = null;
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())){
            int c = dis.read();
            while (c != -1 && c != 10) {
                command += (char) c;
                c = dis.read();
            }

            if (command.startsWith("AUTHENTICATE=")) {
                String userID;
                if (command.contains(",")){
                    String[] commands = command.split("=")[1].split(",");
                    String phoneNumber = commands[0];
                    String password = commands[1];
                    userID = User.authenticate(phoneNumber, password);
                }
                else userID = User.authenticate(command.split("=")[1]);

                if (userID.equals("FAILED"))
                    response = userID;
                else {
                    user = User.getUserByID(userID);
                    response = user.getToken();
                }
            } else if (command.startsWith("SIGN_UP=")) {
                System.out.println(command);
                JsonObject userJson = new JsonParser().parse(command.split("=")[1]).getAsJsonObject();
                try {
                    User new_user = new User(normalize(userJson.get("name").getAsString()),
                            normalize(userJson.get("last_name").getAsString()), userJson.get("phone_number").getAsString(),
                            userJson.get("email").getAsString(), userJson.get("password").getAsString());
                    new_user.save();
                    response = new_user.getToken();
                } catch (RepeatedPhoneNumber e) {
                    response = "PHONE_NUMBER_EXISTED";
                }

            } else {
                String token = command;
                token = token.split("=")[1].split(";")[0];
                command = command.split(";")[1];
                if (!User.authenticate(token).equals("FAILED")) {
                    user = User.getUserByToken(token);
                    if (command.startsWith("GET_ME")) {
                        response = user.toJson(false);
                    } else if (command.startsWith("GET_MY_PRODUCTS")) {
                        ArrayList<String> products = new ArrayList<>();
                        for (Product product : DBHandler.getProducts()) {
                            try {
                                if (product.getSeller().equals(user))
                                    products.add(product.getProductID());
                            } catch (UserDoesNotExists ignored) {
                            }
                        }
                        response = new Gson().toJson(products);
                    } else if (command.startsWith("GET_PRODUCT=")) {
//                        Thread.sleep(500); //Shimmer effect
                        response = Product.getProductByID(command.split("=")[1]).toJson();
                    } else if (command.startsWith("GET_BEST_SELLER")) {
//                        Thread.sleep(500); //Shimmer effect
                        response = Product.getProductByID("a4f2e210f6").toJson();
                    } else if (command.startsWith("GET_NEW_ARRIVALS")) {
                        List<Product> newArrivals = DBHandler.getProducts();
                        Collections.reverse(newArrivals);
                        response = new Gson().toJson(newArrivals.stream().map(Product::getProductID)
                                .limit(4).toList());
                    } else if (command.startsWith("GET_SPECIAL_OFFERS")) {
                        List<String> specialOffers = DBHandler.getProducts().stream()
                                .map(Product::getProductID).toList();
                        List<String> productIDs = new ArrayList<>(specialOffers);
                        Collections.shuffle(productIDs);
                        response = new Gson().toJson(specialOffers.stream().limit(4).collect(Collectors.toList()));
                    } else if (command.startsWith("GET_USER_NAME=")) {
                        try {
                            response = User.getUserByID(command.split("=")[1]).getName();
                        } catch (UserDoesNotExists e) {
                            response = "بهشتی کالا";
                        }
                    } else if (command.startsWith("ADD_ADDRESS=")) {
                        String[] location = command.split("=")[1].split("@ ");
                        user.addAddress(new Address(normalize(location[0]), normalize(location[1])));
                        user.save();
                        response = "DONE";
                    } else if (command.startsWith("SET_ACTIVE_ADDRESS=")) {
                        user.setActiveAddress(Integer.parseInt(command.split("=")[1]));
                        user.save();
                        response = "DONE";
                    } else if (command.startsWith("REMOVE_ADDRESS=")) {
                        user.removeAddress(Integer.parseInt(command.split("=")[1]));
                        user.save();
                        response = "DONE";
                    } else if (command.startsWith("ADD_FAVORITE=")) {
                        user.addFavoriteProduct(command.split("=")[1]);
                        user.save();
                        response = "DONE";
                    } else if (command.startsWith("REMOVE_FAVORITE=")) {
                        user.removeFavoriteProduct(command.split("=")[1]);
                        user.save();
                        response = "DONE";
                    } else if (command.startsWith("GET_FAVORITES")) {
                        response = new Gson().toJson(user.getFavoriteProducts());
                    } else if (command.startsWith("CHANGE_PROFILE=")) {
                        String[] details = command.split("=")[1].split("\\|");
                        user.setName(normalize(details[0]));
                        user.setLastName(normalize(details[1]));
                        user.setPhoneNumber(details[2]);
                        user.setEmail(details[3]);
                        user.save();
                        response = "DONE";
                    } else if (command.startsWith("CHANGE_NAME=")) {
                        user.setName(command.split("=")[1]);
                        user.save();
                        response = "DONE";
                    } else if (command.startsWith("CHANGE_EMAIL=")) {
                        user.setEmail(command.split("=")[1]);
                        user.save();
                        response = "DONE";
                    } else if (command.startsWith("CHANGE_PASSWORD=")) {
                        user.setPassword(command.split("=")[1]);
                        user.save();
                        response = "DONE";
                    } else if (command.startsWith("ADD_COLOR_PRODUCT=")) {
                        String[] p = command.split("=");
                        Product product = Product.getProductByID(p[0]);
                        product.addColor(new Color(p[1], p[2]));
                        product.save();
                        response = "DONE";
                    } else if (command.startsWith("CHANGE_PRODUCT_CATEGORY=")) {
                        String[] p = command.split("=");
                        Product product = Product.getProductByID(p[0]);
                        product.setSubCategory(p[1]);
                        product.save();
                        response = "DONE";
                    } else if (command.startsWith("CHANGE_PRODUCT_PRICE=")) {
                        String[] p = command.split("=");
                        Product product = Product.getProductByID(p[0]);
                        product.setPrice(Integer.parseInt(p[1]));
                        product.save();
                        response = "DONE";
                    } else if (command.startsWith("CHANGE_PRODUCT_IMAGE=")) {
                        String[] p = command.split("=");
                        Product product = Product.getProductByID(p[0]);
                        product.setImage(p[1]);
                        product.save();
                        response = "DONE";
                    } else if (command.equals("GET_CATEGORIES")) {
                        Thread.sleep(250); //Shimmer effect
                        Gson gson = new Gson();
                        Reader reader = Files.newBufferedReader(Paths.get("categories.json"));
                        Map<?, ?> map = gson.fromJson(reader, Map.class);
                        response = new Gson().toJson(map);
                    } else if (command.startsWith("GET_CATEGORY=")) {
                        String id = command.split("=")[1];
                        Gson gson = new Gson();
                        Reader reader = Files.newBufferedReader(Paths.get("categories.json"));
                        Map<?, ?> map = gson.fromJson(reader, Map.class);
                        Map<?, ?> category = (Map<?, ?>) map.get(id);
                        Map<?, ?> subCategories = (Map<?, ?>) category.get("sub_categories");
                        for (Object subCategory : subCategories.keySet()) {
                            ((Map<String, String>) subCategories.get(subCategory)).put("products",gson.toJson(Product.getProductsByCategory(id + "_" + subCategory)));
                        }
                        response = gson.toJson(category);
                    } else if (command.startsWith("GET_PRODUCTS_BY_CATEGORY_ID=")) {
                        String id = command.split("=")[1];
                        response = new Gson().toJson(Product.getProductsByCategory(id));
                    } else if (command.equals("GET_CART")) {
                        response = new Gson().toJson(user.getCart());
                    } else if (command.equals("GET_CART_PRICE")) {
                        int finalPrice = 0;
                        for (String productID : user.getCart()) {
                            finalPrice += Product.getProductByID(productID.split("@")[0]).getPrice() * Integer.parseInt(productID.split("@")[1]);
                        }
                        response = finalPrice + "";
                    } else if (command.startsWith("ADD_TO_CART=")) {
                        String order = command.split("=")[1];
                        ArrayList<String> cart = user.getCart();
                        ArrayList<String> finalCart = new ArrayList<>();
                        boolean isAdded = false;
                        for (String ord : cart) {
                            SingleOrder singleOrder1 = SingleOrder.fromPattern(ord);
                            SingleOrder singleOrder2 = SingleOrder.fromPattern(order);
                            if (singleOrder1.equals(singleOrder2)) {
                                singleOrder1.setAmount(singleOrder1.getAmount() + singleOrder2.getAmount());
                                isAdded = true;
                            }
                            finalCart.add(singleOrder1.toString());
                        }
                        if (!isAdded) finalCart.add(order);
                        user.setCart(finalCart);
                        user.save();
                        response = "DONE";
                    } else if (command.startsWith("REMOVE_FROM_CART=")) {
                        String order = command.split("=")[1];
                        ArrayList<String> cart = user.getCart();
                        ArrayList<String> finalCart = new ArrayList<>();
                        for (String ord : cart) {
                            SingleOrder singleOrder = SingleOrder.fromPattern(ord);
                            if (!singleOrder.equals(SingleOrder.fromPattern(order)))
                                finalCart.add(singleOrder.toString());
                        }
                        user.setCart(finalCart);
                        user.save();
                        response = "DONE";
                    } else if (command.startsWith("REMOVE_FROM_CART_ONCE=")) {
                        String order = command.split("=")[1];
                        ArrayList<String> cart = user.getCart();
                        ArrayList<String> finalCart = new ArrayList<>();
                        for (String ord : cart) {
                            SingleOrder singleOrder = SingleOrder.fromPattern(ord);
                            if (singleOrder.equals(SingleOrder.fromPattern(order)))
                                singleOrder.setAmount(singleOrder.getAmount() - 1);
                            finalCart.add(singleOrder.toString());
                        }
                        user.setCart(finalCart);
                        user.save();
                        response = "DONE";
                    } else if (command.startsWith("FINALIZE_PURCHASE=")) {
                        Map<Product, Integer> stocks = new HashMap<>();
                        for (String pr: user.getCart()) {
                            int amount = Integer.parseInt(pr.split("@")[1]);
                            Product product = Product.getProductByID(pr.substring(0, 10));
                            if (stocks.containsKey(product)) {
                                stocks.put(product, stocks.get(product) + stocks.get(product) + amount);
                            } else {
                                stocks.put(product, amount);
                            }
                        }
                        boolean canBuy = true;
                        for (Product product : stocks.keySet()) {
                            if (product.getStock() < stocks.get(product)) canBuy = false;
                        }
                        if (canBuy) {
                            for (Product product : stocks.keySet()) {
                                product.setStock(product.getStock() - stocks.get(product));
                                product.save();
                            }
                            Order order = new Order(user.getUserID(), user.getCart(), Integer.parseInt(command.split("=")[1]));
                            DBHandler.addOrder(order);
                            user.setCart(new ArrayList<>());
                            user.addOrder(order);
                            user.save();
                            response = "DONE";
                        } else {
                            response = "FAILED";
                        }
                    } else if (command.startsWith("UPDATE_PRODUCT=")) {
                        System.out.println("here");
                        Product product = Product.fromJson(command.replaceAll("UPDATE_PRODUCT=",""));
                        ArrayList<Color> colors = product.getColors();
                        ArrayList<Color> fixedColors = new ArrayList<>();
                        for (Color color : colors) {
                            color.setName(normalize(color.getName()));
                            fixedColors.add(color);
                        }
                        product.setColors(fixedColors);
                        product.setName(normalize(product.getName()));
                        product.setSellerName(normalize(product.getSellerName()));
                        Map<String, String> description = product.getDescription();
                        Map<String, String> fixedDescription = new HashMap<>();
                        boolean isImageSet = false;
                        for (Image image : DBHandler.getImages()) {
                            if (image.getImage().equals(product.getImage())) {
                                product.setImage(image.getId());
                                isImageSet = true;
                            }
                        }
                        if (!isImageSet) {
                            Image image = new Image(product.getImage());
                            image.save();
                            product.setImage(image.getId());
                        }
                        for (String key : description.keySet())
                            fixedDescription.put(normalize(key), normalize(description.get(key)));
                        product.setDescription(fixedDescription);
                        try {
                            product.getSeller();
                        } catch (UserDoesNotExists e) {
                            product.setProductID(Product.getRandomProductID());
                            product.setSeller(user.getUserID());
                        }
                        if (product.getSeller().equals(user)) {
                            product.save();
                            response = "DONE";
                        }
                    } else if (command.startsWith("REMOVE_PRODUCT=")) {
                        DBHandler.removeProduct(Product.getProductByID(command.split("=")[1]));
                        response = "DONE";
                    } else if (command.startsWith("GET_ORDER=")) {
                        response = Order.getOrderByID(command.split("=")[1]).toJson();
                    } else if (command.startsWith("GET_IMAGE=")) {
                        response = Image.getImageByID(command.split("=")[1]).getImage();
                    } else if (command.startsWith("GET_IMAGE_LENGTH=")) {
                        response = String.valueOf(Image.getImageByID(command.split("=")[1]).getImage().length());
                    } else if (command.startsWith("CHANGE_PROFILE_IMAGE=")) {
                        Image image = new Image(command.split("=")[1]);
                        image.save();
                        user.setProfilePicture(image.getId());
                        user.save();
                        response = image.getId();
                    }
                }
            }

            if (command.length() > 0) {
                JUI.changeColor(JUI.Colors.BOLD_YELLOW);
                System.out.print("[" + new Date() + "]" + (user != null ? "" : " "));
                if (user != null) {
                    JUI.changeColor(JUI.Colors.BOLD_CYAN);
                    System.out.print("[" + user.getUserID() + "] ");
                }
                JUI.changeColor(JUI.Colors.BOLD_WHITE);
                System.out.println(command + " -> " + response);
            }
            dos.write(response.getBytes("UTF-8"));
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
