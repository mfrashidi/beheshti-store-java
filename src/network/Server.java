package network;

import exceptions.ProductDoesNotExists;
import exceptions.UserDoesNotExists;
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
        try (DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())){
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
                String phoneNumber = command.split("=")[1].split(", ")[0];
                String password = command.split("=")[1].split(", ")[1];
                String userID = User.authenticate(phoneNumber, password);
                if (userID.equals("FAILED"))
                    response = userID;
                else {
                    response = User.getUserByID(userID).toJson();
                    isAuthenticated = true;
                }
            } if (isAuthenticated) {
                if (command.startsWith("GET_PRODUCT=")) {
                    response = Product.getProductByID(command.split("=")[1]).toJson();
                } else if (command.startsWith("GET_USER_NAME=")) {
                    response = User.getUserByID(command.split("=")[1]).getName();
                }
            }
            JUI.changeColor(JUI.Colors.BOLD_YELLOW);
            System.out.print("> Response: ");
            JUI.changeColor(JUI.Colors.WHITE);
            System.out.println(response);
            dos.writeUTF(response);
            dos.flush();
            System.out.println("------------------------------------------------------------------");
        } catch (IOException | UserDoesNotExists | ProductDoesNotExists e) {
            e.printStackTrace();
        }
    }
}
