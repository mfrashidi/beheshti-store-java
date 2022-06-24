package database;

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

    private static void saveUsers(ArrayList<User> users) {
        try (FileWriter fileWriter = new FileWriter(usersFile)){
            for (User user : users)
                fileWriter.write(user.toJson() + "\n");
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
}
