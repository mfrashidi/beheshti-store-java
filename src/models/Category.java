package models;

import java.util.ArrayList;

public class Category {
    private String name;
    private String id;
    private Color color;
    private String image;
    private boolean hasFadeImage;
    private double imagePadding;
    private double imageWidth;
    private ArrayList<SubCategory> subCategories;

    public static Category getCategoryFromID(String id) {
        return null; // TODO: Handle this
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isHasFadeImage() {
        return hasFadeImage;
    }

    public void setHasFadeImage(boolean hasFadeImage) {
        this.hasFadeImage = hasFadeImage;
    }

    public double getImagePadding() {
        return imagePadding;
    }

    public void setImagePadding(double imagePadding) {
        this.imagePadding = imagePadding;
    }

    public double getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(double imageWidth) {
        this.imageWidth = imageWidth;
    }

    public ArrayList<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
