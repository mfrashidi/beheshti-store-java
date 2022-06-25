package network;

import exceptions.ProductDoesNotExists;
import exceptions.UserDoesNotExists;
import models.Address;
import models.Color;
import models.Product;
import models.User;
import utils.JUI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Server extends Thread{
    private final int PORT = 4536;
    public static void main(String[] args) {
        new Server().start();
    }

    public void run() {
        try {
            JUI.changeColor(JUI.Colors.BOLD_GREEN);
            System.out.println("Server started...");
            JUI.changeColor(JUI.Colors.BOLD_CYAN);
            System.out.println(new Date() + "\n");
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
        boolean isAuthenticated = false;
        String command;
        String response = "";
        User user = null;
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())){
            while (true) {
                command = dis.readUTF();
                JUI.changeColor(JUI.Colors.BOLD_YELLOW);
                System.out.print("> Time: ");
                JUI.changeColor(JUI.Colors.WHITE);
                System.out.println(new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
                JUI.changeColor(JUI.Colors.BOLD_YELLOW);
                System.out.print("> Command: ");
                JUI.changeColor(JUI.Colors.WHITE);
                System.out.println(command);
                if (command.startsWith("AUTHENTICATE=")) {
                    String userID;
                    if (command.contains(",")){
                        String phoneNumber = command.split("=")[1].split(", ")[0];
                        String password = command.split("=")[1].split(", ")[1];
                        userID = User.authenticate(phoneNumber, password);
                    }
                    else userID = User.authenticate(command.split("=")[1]);

                    if (userID.equals("FAILED"))
                        response = userID;
                    else {
                        user = User.getUserByID(userID);
                        response = user.getToken();
                        isAuthenticated = true;
                    }
                } if (isAuthenticated) {
                    if (command.startsWith("GET_USER=")) {
                        response = User.getUserByToken(command.split("=")[1]).toJson();
                    } else if (command.startsWith("GET_PRODUCT=")) {
                        response = Product.getProductByID(command.split("=")[1]).toJson();
                    } else if (command.startsWith("GET_USER_NAME=")) {
                        response = User.getUserByID(command.split("=")[1]).getName();
                    } else if (command.startsWith("ADD_LOCATION=")) {
                        String location = command.split("=")[1];
                        user.addAddress(new Address(location.split("; ")[0], location.split("; ")[1]));
                        user.save();
                        response = "DONE";
                    } else if (command.startsWith("ADD_FAVORITE=")) {
                        user.addFavoriteProduct(Product.getProductByID(command.split("=")[1]));
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
                    }
                }
                JUI.changeColor(JUI.Colors.BOLD_YELLOW);
                System.out.print("> Response: ");
                JUI.changeColor(JUI.Colors.WHITE);
                System.out.println(response);
                dos.writeUTF(response);
                dos.flush();
                System.out.println("------------------------------------------------------------------");
            }
        } catch (IOException | UserDoesNotExists | ProductDoesNotExists e) {
            e.printStackTrace();
        }
    }
}
