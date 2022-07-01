package models;

import java.util.Objects;

public class SingleOrder {
    private String productID;
    private int amount;
    private int colorIndex;
    private int sizeIndex;

    public SingleOrder(String productID, int amount, int colorIndex, int sizeIndex) {
        this.productID = productID;
        this.amount = amount;
        this.colorIndex = colorIndex;
        this.sizeIndex = sizeIndex;
    }

    public static SingleOrder fromPattern(String text) {
        String[] datas = text.split("@");
        return new SingleOrder(datas[0], Integer.parseInt(datas[1]),
                Integer.parseInt(datas[2]), Integer.parseInt(datas[3]));
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public int getSizeIndex() {
        return sizeIndex;
    }

    public void setSizeIndex(int sizeIndex) {
        this.sizeIndex = sizeIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleOrder that = (SingleOrder) o;
        return colorIndex == that.colorIndex && sizeIndex == that.sizeIndex && Objects.equals(productID, that.productID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productID, colorIndex, sizeIndex);
    }

    @Override
    public String toString() {
        return productID + "@" + amount + "@" + colorIndex + "@" + sizeIndex;
    }
}
