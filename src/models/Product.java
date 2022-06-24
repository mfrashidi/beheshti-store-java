package models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

public class Product {
    private String name;
    private int price;
    @SerializedName("product_id")
    private String productID;
    private String image;
    @SerializedName("images_count")
    private int imagesCount;
    @SerializedName("sub_category")
    private SubCategory subCategory;
    private int stars;
    @SerializedName("has_color")
    private boolean hasColor;
    private ArrayList<Color> colors;
    private Map<String, String> description;
    private User seller;
    @SerializedName("has_size")
    private boolean hasSize;
    private ArrayList<Integer> sizes;

    public static Product getProductByID(String id) {
        return null; // TODO: Handle this
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getImagesCount() {
        return imagesCount;
    }

    public void setImagesCount(int imagesCount) {
        this.imagesCount = imagesCount;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public boolean isHasColor() {
        return hasColor;
    }

    public void setHasColor(boolean hasColor) {
        this.hasColor = hasColor;
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    public void setColors(ArrayList<Color> colors) {
        this.colors = colors;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public boolean isHasSize() {
        return hasSize;
    }

    public void setHasSize(boolean hasSize) {
        this.hasSize = hasSize;
    }

    public ArrayList<Integer> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<Integer> sizes) {
        this.sizes = sizes;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static Product fromJson(String json) {
        return new Gson().fromJson(json, Product.class);
    }
}
