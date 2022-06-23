package models;

public class SubCategory {
    private String name;
    private String id;
    private Category category;
    private String icon;

    public SubCategory(String name, String id, String categoryID, String icon) {
        this.name = name;
        this.id = id;
        this.category = Category.getCategoryFromID(categoryID);
        this.icon = icon;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
