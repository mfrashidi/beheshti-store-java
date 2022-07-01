package models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import database.DBHandler;
import exceptions.ProductDoesNotExists;
import exceptions.UserDoesNotExists;

import java.util.*;

public class Product {
    private String name;
    private int price;
    @SerializedName("product_id")
    private String productID;
    private String image;
    @SerializedName("images_count")
    private int imagesCount;
    @SerializedName("sub_category")
    private String subCategory;
    private int stars;
    @SerializedName("has_color")
    private boolean hasColor;
    private int stock;
    private ArrayList<Color> colors = new ArrayList<>();
    private Map<String, String> description = new HashMap<>();
    private String seller;
    @SerializedName("seller_name")
    private String sellerName;
    @SerializedName("has_size")
    private boolean hasSize;
    private ArrayList<Double> sizes = new ArrayList<>();


    public static Product getProductByID(String productID) throws ProductDoesNotExists {
        for (Product product : DBHandler.getProducts())
            if (product.getProductID().equals(productID))
                return product;
        throw new ProductDoesNotExists();
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

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
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

    public void addColor(Color color) {
        colors.add(color);
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public User getSeller() throws UserDoesNotExists {
        return User.getUserByID(seller);
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public boolean isHasSize() {
        return hasSize;
    }

    public void setHasSize(boolean hasSize) {
        this.hasSize = hasSize;
    }

    public ArrayList<Double> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<Double> sizes) {
        this.sizes = sizes;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public static ArrayList<String> getProductsByCategory(String id) {
        ArrayList<Product> products = DBHandler.getProducts();
        ArrayList<String> productIDs = new ArrayList<>();
        for (Product product : products)
            if (product.getSubCategory().startsWith(id))
                productIDs.add(product.getProductID());
        return productIDs;
    }

    public static String getRandomProductID(){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < 10){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, 10);
    }

    public String toJson() {
        hasColor = !colors.isEmpty();
        hasSize = !sizes.isEmpty();
//        name = unNormalize(name);
//        Map<String, String> desc = new HashMap<>();
//        for (String d : description.keySet())
//            desc.put(unNormalize(d), unNormalize(description.get(d)));
//        description = desc;
//        ArrayList<Color> colorArrayList = new ArrayList<>();
//        for (Color color : colors) {
//            color.setName(unNormalize(color.getName()));
//            colorArrayList.add(color);
//        }
//        colors = colorArrayList;
        return new Gson().toJson(this);
    }

    public static Product fromJson(String json) {
        return new Gson().fromJson(json, Product.class);
    }

    public void save() {
        DBHandler.removeProduct(this);
        DBHandler.addProducts(this);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productID.equals(product.productID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productID);
    }
}
