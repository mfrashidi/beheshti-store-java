package models;

import com.google.gson.Gson;
import database.DBHandler;
import exceptions.ImageDoesNotExists;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Image {
    private String id;
    private String image;

    public Image(String image) {
        this.image = image;
        this.id = Image.generateID();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static String generateID(){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < 10){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, 10);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static Image fromJson(String json) {
        return new Gson().fromJson(json, Image.class);
    }

    public void save() {
        DBHandler.removeImage(this);
        DBHandler.addImage(this);
    }

    public static Image getImageByID(String id) throws ImageDoesNotExists {
        for (Image image : DBHandler.getImages())
            if(image.getId().equals(id))
                return image;
        throw new ImageDoesNotExists();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(id, image.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
